package net.artux.sendler.service.inboxviewer;

import com.sun.mail.imap.IMAPInputStream;
import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Service;
import net.artux.sendler.entity.settings.UserMailEntity;
import net.artux.sendler.service.sender.SelfSenderService;
import net.artux.sendler.service.sender.SenderServiceImpl;

import javax.mail.*;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Service
@RequiredArgsConstructor
public class InboxViewerServiceImpl implements InboxViewerService {

    private final Log logger = LogFactory.getLog(SenderServiceImpl.class);
    private final SelfSenderService selfSenderService;

    @Override
    public List<String> checkInvalidAddress(UserMailEntity userMailEntity) {
        Store store = null;
        Folder folder = null;
        List<String> invalidAddresses = new LinkedList<>();
        try {
            Properties props = System.getProperties();
            props.put("mail.store.protocol", "imaps");
            Session session = Session.getDefaultInstance(props, null);
            store = session.getStore("imaps");

            //TODO the imap host is hardcoded because there is no front interface and db field to set and save it
            store.connect("imap.yandex.com", userMailEntity.getUsername(), userMailEntity.getPassword());
            folder = store.getFolder("Inbox"); //Mail Sendler
            folder.open(Folder.READ_WRITE);
            int unreadMessagesCount = folder.getUnreadMessageCount();
            if (unreadMessagesCount > 0) {
                Message[] messages = folder.getMessages();
                List<Message> unreadMessages = Arrays.stream(messages).filter(message -> {
                    try {
                        return !message.isSet(Flags.Flag.SEEN);
                    } catch (MessagingException messagingException) {
                        logger.error(messagingException.getMessage(), messagingException);
                    }
                    return false;
                }).toList();
                for (Message message : unreadMessages) {
                    String subject = message.getSubject();
                    if (Objects.equals(subject, "Недоставленное сообщение")) {
                        Object content = message.getContent();
                        if (content instanceof MimeMultipart mimeMultipartContent) {
                            int partsCount = mimeMultipartContent.getCount();
                            for (int partIndex = 0; partIndex < partsCount; partIndex++) {
                                BodyPart part = mimeMultipartContent.getBodyPart(partIndex);
                                Object partContent = part.getContent();
                                String partText;
                                if (partContent instanceof IMAPInputStream) {
                                    InputStream inputStream = (InputStream) partContent;
                                    byte[] bytes = inputStream.readAllBytes();
                                    partText = new String(bytes);
                                    partText = partText.substring(partText.indexOf("Original-Recipient:"));
                                    partText = partText.substring(0, partText.indexOf('\n') - 1);
                                    partText = partText.substring(partText.indexOf("rfc822;") + 7);
                                    partText = partText.strip();
                                    invalidAddresses.add(partText);
                                    message.setFlag(Flags.Flag.SEEN, true);
                                    // message.saveChanges();
                                }
                            }
                        }
                    }
                }
            }
        } catch (Throwable throwable) {
            logger.error(throwable.getMessage(), throwable);
        }
        return invalidAddresses;
    }

    private int[] getConfigsFromMessage(Message message, String[] configNames) throws MessagingException, IOException {
        int[] configs = {3, 0, 0, 500};
        Object content = message.getContent();
        String configText;
        if (content instanceof MimeMultipart mimeMultipartContent) {
            BodyPart part = mimeMultipartContent.getBodyPart(0);
            configText = (String) part.getContent();
        } else {
            configText = (String) content;
        }
        configText = Jsoup.parse(configText).text();
        configText = configText.substring(0, configText.indexOf("###"));
        configText = configText.replaceAll(" ", "");
        configText = configText.toLowerCase();
        for (int configIndex = 0; configIndex < configs.length; configIndex++) {
            String configName = configNames[configIndex];
            int configStringIndex = configText.indexOf(configName);
            if (configStringIndex == -1) {
                continue;
            }
            String configString = configText.substring(configStringIndex);
            configString = configString.substring(0, configString.indexOf(";"));
            configs[configIndex] = Integer.parseInt(configString.substring(configString.indexOf("=") + 1));
        }
        return configs;
    }

    @Override
    public int[] getConfigs(UserMailEntity userMailEntity) {
        Store store = null;
        Folder folder = null;
        int[] configs = {3, 0, 0, 500};
        String[] configNames = {
                "нормальнаязадержка",
                "периоддлиннойзадержки",
                "длиннаязадержка",
                "ограничениеотправки"};
        boolean configMsgFound = false;
        boolean flaggedConfigMsgFound = false;
        try {
            Properties props = System.getProperties();
            props.put("mail.store.protocol", "imaps");
            Session session = Session.getDefaultInstance(props, null);
            store = session.getStore("imaps");

            //TODO the imap host is hardcoded because there is no front interface and db field to set and save it
            store.connect("imap.yandex.com", userMailEntity.getUsername(), userMailEntity.getPassword());
            folder = store.getFolder("Inbox");
            folder.open(Folder.READ_WRITE);
            Message[] messages = folder.getMessages();
            for (Message message : messages) {
                String subject = message.getSubject();
                if (Objects.equals(subject.toLowerCase(), "MailSendler Конфигурация".toLowerCase())) {
                    configMsgFound = true;
                    if (message.isSet(Flags.Flag.FLAGGED)) {
                        flaggedConfigMsgFound = true;
                        configs = getConfigsFromMessage(message, configNames);
                        break;
                    }
                }
            }
            if (!configMsgFound) {
                selfSenderService.sendMailOnUserWorkAddress(
                        userMailEntity,
                        "MailSendler Конфигурация",
                        """
                                Нормальная задержка = 3 ;
                                Период длинной задержки = 0 ;
                                Длинная задержка = 0 ;
                                Ограничение отправки = 500 ;
                                                                
                                                                
                                ###
                                                                
                                Нормальная задержка — задержка между каждым сообщением в секундах
                                Период длинной задержки — кол-во сообщений между которыми будет длинная задержка, 0 = откл.
                                Длинная задержка — длинная задержка в секундах
                                Ограничение отправки — максимальное кол-во сообщений, которое можно отправить за раз
                                Кол-во секунд — целое неотрицательное число
                                                                
                                Вы можете иметь несколько сообщений конфигурации, помечайте флажком нужное
                                Не забывайте ставить точку с запятой " ; " в конце""");
            }
            if (!flaggedConfigMsgFound) {
                for (Message message : messages) {
                    if (Objects.equals(message.getSubject(), "MailSendler Конфигурация")) {
                        message.setFlag(Flags.Flag.FLAGGED, true);
                        configs = getConfigsFromMessage(message, configNames);
                        break;
                    }
                }
            }
        } catch (Throwable throwable) {
            logger.error(throwable.getMessage(), throwable);
        }
        return configs;
    }

}
