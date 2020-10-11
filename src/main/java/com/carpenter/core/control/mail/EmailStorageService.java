package com.carpenter.core.control.mail;

import com.carpenter.core.entity.email.EmailStatus;
import com.carpenter.core.entity.email.EmailStorage;
import lombok.extern.slf4j.Slf4j;

import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Properties;

@Slf4j
@Stateless
public class EmailStorageService implements Serializable {

    private static final long serialVersionUID = 5159251152579118681L;

    private static final String SANDER_INFO_MAIL = "podkarpaccy.ciesle@gmail.com";
    private static final String SENDING_EMAIL_ERROR_MESSAGE = "Error sending e-mail";
    private static final String EMAIL_PASSWORD = System.getProperty("emailPassword");

    @Inject
    private EmailStorageRepository emailStorageRepository;

    public void saveEmail(String recipients, String content) {
        EmailStorage emailStorage = new EmailStorage(recipients, "Oferta", content, Boolean.FALSE, new Date(), EmailStatus.NOT_SENT, 0);
        emailStorage.setCreateBy("system");
        emailStorage.setCreateDate(new Date());
        emailStorageRepository.persistEmail(emailStorage);
    }

    @Asynchronous
    public void sandEmail(EmailStorage emailStorage) {
        log.info("Dispatching email with subject \"{}\" and contents: \"{}\" to: {}", emailStorage.getSubject(), emailStorage.getContent(), emailStorage.getRecipients());

        Properties properties = new Properties();

        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.transport.protocol", "smtp");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.disable", "false");

        Session session = Session.getInstance(properties,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(SANDER_INFO_MAIL, EMAIL_PASSWORD);
                    }
                });

        MimeMessage message = new MimeMessage(session);

        try {
            message.setSender(new InternetAddress(SANDER_INFO_MAIL));
            message.setRecipients(Message.RecipientType.TO, emailStorage.getRecipients());
            message.setSubject(emailStorage.getSubject());
            message.setContent(emailStorage.getContent(), "text/html; charset=utf-8");

            Transport.send(message);
            log.info("Email sent: {}", message);
            emailStorage.setSent(Boolean.TRUE);
            emailStorage.setStatus(EmailStatus.SENT);
            emailStorage.setCommitDate(new Date());
            emailStorage.setAttempts(emailStorage.getAttempts() + 1);
        } catch (MessagingException e) {
            log.error(SENDING_EMAIL_ERROR_MESSAGE);
            emailStorage.setSent(Boolean.FALSE);
            emailStorage.setStatus(EmailStatus.NOT_SENT);
            emailStorage.setCommitDate(new Date());
            emailStorage.setAttempts(emailStorage.getAttempts() + 1);
        } finally {
            emailStorageRepository.updateEmail(emailStorage);
        }
    }

    public List<EmailStorage> getAllNotSendedEmail() {
        return emailStorageRepository.findAllNoSendedEmails();
    }
}
