package com.banchango.common.service;

import com.banchango.common.dto.BasicMessageResponseDto;
import com.banchango.common.exception.InternalServerErrorException;
import com.banchango.tools.EmailContent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Properties;

@Component
public class EmailSender {
    @Value("${banchango.email}")
    private String senderEmail;

    @Value("${banchango.email.password}")
    private String password;

    @Value("${banchango.email.debug}")
    private boolean isDebugAllowed;

    public BasicMessageResponseDto send(String recipient, EmailContent emailContent, boolean isAdminIncluded) {
        try {
            String host = "smtp.gmail.com";

            Properties properties = new Properties();
            properties.put("mail.transport.protocol", "smtp");
            properties.put("mail.smtp.host", host);
            properties.put("mail.smtp.port", 465);
            properties.put("mail.smtp.starttls.enable", "true");

            properties.put("mail.debug", "" + isDebugAllowed);

            properties.put("defaultEncoding", "utf-8");
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.ssl.enable", "true");
            properties.put("mail.smtp.ssl.trust", "smtp.gmail.com");

            Session session = Session.getDefaultInstance(properties, new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(senderEmail, password);
                }
            });

            String content = EmailContent.SimpleTemplate(emailContent);

            MimeMessage message = new MimeMessage(session);

            Multipart multipart = new MimeMultipart();
            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setText(content, "UTF-8", "html");
            multipart.addBodyPart(mimeBodyPart);
            message.setContent(multipart);
            message.setFrom(new InternetAddress(senderEmail));

            if(isAdminIncluded) {
                message.setRecipients(Message.RecipientType.TO, new Address[]{new InternetAddress(recipient), new InternetAddress("wherehousegm@gmail.com")});
            } else {
                message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
            }

            message.setSubject(emailContent.getTitle());
            Transport.send(message);

            return new BasicMessageResponseDto("이메일이 정상적으로 전송되었습니다.");
        } catch(MessagingException exception) {
            throw new InternalServerErrorException(exception.getMessage());
        }
    }
}