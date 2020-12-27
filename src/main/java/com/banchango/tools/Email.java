package com.banchango.tools;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class Email {
    public static void sendEmail(EmailContent emailContent, String recepeint, String senderEmail, String password, boolean isDebugAllowed) throws MessagingException {

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
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(recepeint));
        message.setSubject(emailContent.getTitle());
        Transport.send(message);
    }
}
