package com.carpenter.core.control.mail;

import com.carpenter.core.entity.email.EmailStorage;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.List;

@Slf4j
@Singleton
@Startup
public class EmailSenderScheduler implements Serializable {

    private static final long serialVersionUID = 3593447537124278929L;

    @Inject
    private EmailStorageService emailStorageService;

    @Schedule(minute = "*/5", hour = "*", persistent = false)
    public void sendMails(){
        List<EmailStorage> notSendedEmails = emailStorageService.getAllNotSendedEmail();
        for (EmailStorage mail : notSendedEmails) {
            emailStorageService.sandEmail(mail);
        }
        log.info("Finish sent emails!" + notSendedEmails);
    }
}
