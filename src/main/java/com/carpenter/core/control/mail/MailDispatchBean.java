package com.carpenter.core.control.mail;

import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.Serializable;
import java.util.Properties;

@Slf4j
@Stateless
public class MailDispatchBean implements Serializable {

    private static final long serialVersionUID = 4334946629620090091L;

    private static final String SANDER_INFO_MAIL = "podkarpaccy.ciesle@gmail.com";
    private static final String SENDING_EMAIL_ERROR_MESSAGE = "Error sending e-mail";

    @Asynchronous
    public void sandEmail(String recipient, String subject, String content) {
        log.info("Dispaching email with subject \"{}\" and contents: \"{}\" to: {}", subject, content, recipient);

        final String username = "podkarpaccy.ciesle@gmail.com";
        final String password = "szczescia20";

        String host = "smtp.gmail.com";

        Properties properties = new Properties();

        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.host", host);
        properties.put("mail.transport.protocol", "smtp");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.disable", "false");

        Session session = Session.getInstance(properties,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        MimeMessage message = new MimeMessage(session);

        try {
            message.setSender(new InternetAddress(SANDER_INFO_MAIL));
            message.setRecipients(Message.RecipientType.TO, recipient);
            message.setSubject(subject);
            message.setContent(content, "text/plain;charset=utf-8");

            Transport.send(message);
            log.info("Email sent");
        } catch (MessagingException e) {
            log.error(SENDING_EMAIL_ERROR_MESSAGE);
        }
    }

    public void sandEmailToManager(String content) {
        sandEmail(SANDER_INFO_MAIL, "Oferta", content);
    }


}
