package net.artux.mupse.service.sender;

import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import net.artux.mupse.entity.BaseEntity;
import net.artux.mupse.entity.contact.ContactEntity;
import net.artux.mupse.entity.contact.ContactGroupEntity;
import net.artux.mupse.entity.mailing.MailingEntity;
import net.artux.mupse.entity.mailing.MailingStatus;
import net.artux.mupse.entity.settings.UserMailEntity;
import net.artux.mupse.entity.statistic.MailingEventEntity;
import net.artux.mupse.entity.statistic.MailingResultEntity;
import net.artux.mupse.entity.user.UserEntity;
import net.artux.mupse.model.contact.ContactGroupCreateDto;
import net.artux.mupse.model.contact.ContactGroupDto;
import net.artux.mupse.repository.contact.ContactRepository;
import net.artux.mupse.repository.mailing.MailingRepository;
import net.artux.mupse.repository.mailing.MailingResultRepository;
import net.artux.mupse.repository.settings.UserMailRepository;
import net.artux.mupse.repository.statistic.MailingEventRepository;
import net.artux.mupse.service.contact.GroupContactService;
import net.artux.mupse.service.inboxviewer.InboxViewerService;
import net.artux.mupse.service.parameter.ParameterService;
import net.artux.mupse.service.user.UserService;

import javax.mail.*;
import javax.mail.internet.MimeMessage;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.Future;

@Service
@RequiredArgsConstructor
public class SenderServiceImpl implements SenderService {

    private final Log logger = LogFactory.getLog(SenderServiceImpl.class);
    private final ContactRepository contactRepository;
    private final MailingRepository mailingRepository;
    private final UserMailRepository userMailRepository;
    private final MailingEventRepository mailingEventRepository;
    private final MailingResultRepository mailingResultRepository;

    private final UserService userService;
    private final ParameterService parameterService;
    private final TemplateEngine templateEngine;
    private final InboxViewerService inboxViewerService;
    private final GroupContactService groupContactService;
    private final SelfSenderService selfSenderService;

    @Value("${server.protocol}")
    private String protocol;
    @Value("${server.host}")
    private String host;
    @Value("${server.servlet.contextPath}")
    private String contextPath;
    @Value("${debug:false}")
    private Boolean debug;
    private String url;

    private JavaMailSender getInstance(UserMailEntity entity) {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(entity.getHost());
        mailSender.setPort(entity.getPort());

        mailSender.setUsername(entity.getUsername());
        mailSender.setPassword(entity.getPassword());

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.debug", debug);

        return mailSender;
    }

    private void createRestContactGroup(MailingEntity mailingEntity, List<ContactEntity> contacts, int contactIterator) {
        ContactGroupEntity firstGroup = mailingEntity.getGroups().iterator().next();
        String firstGroupName = firstGroup.getName();
        if (firstGroupName.contains("ОСТАТОК")) {
            int ostIndex = firstGroupName.indexOf("ОСТАТОК");
            int ostNum = Integer.parseInt(firstGroupName.substring(ostIndex + 7).strip());
            firstGroupName = firstGroupName.substring(0, ostIndex + 7) + " " + ++ostNum;
        } else {
            firstGroupName += " ОСТАТОК 1";
        }
        ContactGroupCreateDto newRestGroup = new ContactGroupCreateDto();
        newRestGroup.setName(firstGroupName);
        newRestGroup.setHexColor(firstGroup.getHexColor());
        newRestGroup.setDescription(firstGroup.getDescription());
        try {
            ContactGroupDto createdGroup = groupContactService.createGroup(newRestGroup);
            List<Long> restContactIds = contacts.subList(contactIterator,contacts.size())
                    .stream()
                    .map(ContactEntity::getId)
                    .toList();
            groupContactService.putContacts(createdGroup.getId(), restContactIds);
        } catch (RuntimeException runtimeException) {
            logger.error(runtimeException.getMessage());
        }
    }

    @Async
    @Override
    @Transactional // для доступа к субд из потока
    public Future<MailingResultEntity> startMailing(Long id) {
        url = protocol + "://" + host + contextPath; //todo remove

        MailingEntity mailingEntity = mailingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Не удалось найти рассылку."));
        LinkedList<MailingEventEntity> events = new LinkedList<>();
        List<ContactEntity> contacts = contactRepository
                .findAllByGroups(mailingEntity.getGroups().stream().mapToLong(BaseEntity::getId).toArray());
        mailingEntity.setToken(UUID.randomUUID());
        int attempts = 0;
        int success = 0;
        boolean anyProblems = false;
        try {
            int[] configs = inboxViewerService.getConfigs(mailingEntity.getMail());
            int eachMessageDelay = configs[0];
            int longDelayMessagesCount = configs[1];
            int longDelay = configs[2];
            int maxMessagesCount = configs[3];

            JavaMailSender mailSender = getInstance(mailingEntity.getMail());
            final Context ctx = new Context(Locale.ROOT);
            MimeMessage message = mailSender.createMimeMessage();
            int separationIterator = 0;
            for (int contactIterator = 0; contactIterator < contacts.size(); contactIterator++) {
                ContactEntity contact = contacts.get(contactIterator);
                if (!contact.isDisabled()) {
                    Thread.sleep(eachMessageDelay * 1000);
                    if (longDelayMessagesCount > 0) {
                        separationIterator++;
                        if (separationIterator >= longDelayMessagesCount) {
                            Thread.sleep(longDelay * 1000);
                        }
                    }
                    if (attempts >= maxMessagesCount) {
                        createRestContactGroup(mailingEntity, contacts, contactIterator);
                        break;
                    }
                    attempts++;
                    try {
                        String htmlPage = getHtmlLetter(ctx, mailingEntity.getContent(), mailingEntity, contact);
                        final MimeMessageHelper messageHelper =
                                new MimeMessageHelper(message, true, "UTF-8");
                        messageHelper.setFrom(mailingEntity.getNameFrom() + " <" + mailingEntity.getMail().getUsername() + ">");
                        messageHelper.setTo(contact.getEmail());
                        messageHelper.setSubject(parameterService.putParameters(mailingEntity.getSubject(), contact));
                        messageHelper.setText(htmlPage, true);
                        message.setHeader("List-Unsubscribe", String.valueOf(ctx.getVariable("unsubscribeLink")));
                        message.setHeader("List-Unsubscribe-Post", "List-Unsubscribe=One-Click");
                        mailSender.send(message);
                        success++;
                    } catch (MessagingException e) {
                        anyProblems = true;
                        e.printStackTrace();
//                        MailingEventEntity event = new MailingEventEntity();
//                        event.setMailing(entity);
//                        event.setContact(contact);
//                        event.setContent(e.getLocalizedMessage());
//                        events.add(event);
                        if (e instanceof AuthenticationFailedException)
                            break;
                    } catch (MailSendException mailSendException) {  // SPAM Warning Handler
                        logger.error(mailSendException.getMessage());
//                        MailingEventEntity event = new MailingEventEntity();
//                        event.setMailing(entity);
//                        event.setContact(contact);
//                        event.setContent(mailSendException.getLocalizedMessage());
//                        events.add(event);
                        createRestContactGroup(mailingEntity, contacts, contactIterator);
                        break;
                    }
                    logger.info("Successful sent emails: " + success);
                }
            }
        } catch (Throwable throwable) {
            anyProblems = true;
            throwable.printStackTrace();
            logger.error(throwable.getMessage());
        } finally {
            mailingEventRepository.saveAll(events);
            mailingEntity.setStatus(/*anyProblems ? MailingStatus.FAILURE : */MailingStatus.DONE);
            mailingRepository.save(mailingEntity);
        }
        try {  // Проверка колбеков от яндекса
            Thread.sleep(1000 * 5);
            List<String> badAddresses = inboxViewerService.checkInvalidAddress(mailingEntity.getMail());
            if (badAddresses.size() > 0) {
                for (String badAddress : badAddresses) {
                    contactRepository.deleteAllByEmail(badAddress);
                }
                logger.info("Bad addresses founded: " + badAddresses);
                StringBuilder messageText = new StringBuilder("Удалены контакты со следующими недействительными адресами:\n\n");
                for (String badAddress : badAddresses) {
                    messageText.append(badAddress).append('\n');
                }
                selfSenderService.sendMailOnUserWorkAddress(mailingEntity.getMail(), "Обнаружены недействительные адреса", messageText.toString());
            }
        }
        catch (InterruptedException interruptedException) {
            logger.error("Incoming messages not checked (" + interruptedException.getMessage() + ")");
        }
        catch (Throwable throwable) {
            logger.error(throwable.getMessage());
        }
        return new AsyncResult<>(saveResult(mailingEntity, contacts.size(), attempts, success));
    }

    @Override
    public boolean testMail(Long id) {
        UserEntity entity = userService.getUserEntity();
        UserMailEntity mailEntity = userMailRepository.findByOwnerAndId(entity, id).orElseThrow();
        JavaMailSender mailSender = getInstance(mailEntity);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("Тестовое сообщение <" + mailEntity.getUsername() + ">");
        message.setTo(entity.getEmail());
        message.setSubject("Тест");
        message.setText("Тестовое сообщение, если вы его видите, значит настройки для "
                + mailEntity.getUsername() + " заданы верно.");
        mailSender.send(message);

        return true;
    }

    private String getHtmlLetter(Context context, String content, MailingEntity mailing, ContactEntity contact) {
        content = replaceLinks(content, mailing.getToken().toString(), contact.getToken().toString());
        String statisticUrl = url + "/statistic/opened/" + mailing.getToken() + "/" + contact.getToken();
        context.setVariable("statistic", statisticUrl);
        context.setVariable("content", parameterService.putParameters(content, contact));

        String unsubscribeLink = url + "/subs/unsubscribe/" + contact.getToken();
        context.setVariable("unsubscribeLink", unsubscribeLink);

        return templateEngine.process("mail.html", context);
    }

    public MailingResultEntity saveResult(MailingEntity entity, int contacts, int attempts, int success) {
        MailingResultEntity mailingResult = new MailingResultEntity();
        mailingResult.setMailing(entity);
        mailingResult.setContacts(contacts);
        mailingResult.setSuccess(success);
        mailingResult.setAttempts(attempts);

        return mailingResultRepository.save(mailingResult);
    }

    public String replaceLinks(String content, String mailingToken, String contactToken) {
        Document document = Jsoup.parse(content);
        Elements anchors = document.getElementsByTag("a");
        String base = url + "/statistic/redirect/" + mailingToken + "/" + contactToken;
        for (org.jsoup.nodes.Element anchor : anchors) {
            String oldLink = anchor.attr("href");
            String link = URLEncoder.encode(oldLink, StandardCharsets.UTF_8);
            anchor.attr("href", base + "?to=" + link);
        }
        return document.outerHtml();
    }
}
