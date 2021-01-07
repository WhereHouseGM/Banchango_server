package com.banchango.common.service;

import com.banchango.common.dto.BasicMessageResponseDto;
import com.banchango.common.exception.InternalServerErrorException;
import com.banchango.tools.EmailContent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.*;

import javax.annotation.PostConstruct;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Properties;

@Component
public class EmailSender {

    private SesClient sesClient;

    @Value("${aws.ses.access_key_id}")
    private String accessKey;

    @Value("${aws.ses.secret_access_key}")
    private String secretAccessKey;

    @PostConstruct
    public void setSesClient() {
        sesClient = SesClient.builder()
                .credentialsProvider(() -> AwsBasicCredentials.create(accessKey, secretAccessKey))
                .region(Region.AP_NORTHEAST_2).build();
    }

    public BasicMessageResponseDto send(String recipient, EmailContent emailContent, boolean isAdminIncluded) {

        try {
            Session session = Session.getDefaultInstance(new Properties());
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress("no-reply@banchangohub.com"));
            message.setSubject("[반창고]");
            message.setSender(new InternetAddress("no-reply@banchangohub.com"));
            message.setReplyTo(new Address[]{new InternetAddress("wherehousegm@gmail.com")});
            if(isAdminIncluded) {
                message.setRecipients(Message.RecipientType.TO, new Address[]{new InternetAddress(recipient), new InternetAddress("wherehousegm@gmail.com")});
            } else {
                message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
            }
            String content = EmailContent.SimpleTemplate(emailContent);

            Multipart multipart = new MimeMultipart();
            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setText(content, "UTF-8", "html");
            multipart.addBodyPart(mimeBodyPart);
            message.setContent(multipart);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            message.writeTo(outputStream);
            ByteBuffer byteBuffer = ByteBuffer.wrap(outputStream.toByteArray());

            byte[] byteArr = new byte[byteBuffer.remaining()];
            byteBuffer.get(byteArr);

            SdkBytes data = SdkBytes.fromByteArray(byteArr);

            RawMessage rawMessage = RawMessage.builder()
                    .data(data).build();

            SendRawEmailRequest rawEmailRequest = SendRawEmailRequest.builder()
                    .rawMessage(rawMessage).destinations(recipient).build();

            sesClient.sendRawEmail(rawEmailRequest);

            return new BasicMessageResponseDto("이메일이 정상적으로 전송되었습니다.");

        } catch(MessagingException | IOException exception) {
            throw new InternalServerErrorException(exception.getMessage());
        }
    }
}
