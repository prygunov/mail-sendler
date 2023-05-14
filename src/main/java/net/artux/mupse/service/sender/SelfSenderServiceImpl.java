package net.artux.mupse.service.sender;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;
import net.artux.mupse.entity.settings.UserMailEntity;

import java.util.Properties;

@Service
@RequiredArgsConstructor
public class SelfSenderServiceImpl implements SelfSenderService {

    @Value("${debug:false}")
    private Boolean debug;

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

    @Override
    public boolean sendMailOnUserWorkAddress(UserMailEntity userMailEntity, String subject, String text) {
        JavaMailSender mailSender = getInstance(userMailEntity);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("MailSendler <" + userMailEntity.getUsername() + ">");
        message.setTo(userMailEntity.getUsername());
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
        return true;
    }
}
