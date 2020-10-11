package com.carpenter.core.control.mail;

import com.carpenter.core.entity.email.EmailStorage;
import lombok.extern.slf4j.Slf4j;

import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.Serializable;
import java.util.List;
import java.util.Properties;

@Slf4j
@Stateless
public class EmailStorageBean implements Serializable {

    private static final long serialVersionUID = 4334946629620090091L;

    @Inject
    private EmailStorageService emailStorageService;

    private static final String SANDER_INFO_MAIL = "podkarpaccy.ciesle@gmail.com";

    public void saveEmail(String content) {
        emailStorageService.saveEmail(SANDER_INFO_MAIL, content);
    }

}
