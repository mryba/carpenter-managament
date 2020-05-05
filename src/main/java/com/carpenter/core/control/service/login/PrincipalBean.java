package com.carpenter.core.control.service.login;

import com.carpenter.core.entity.employee.Employer;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.security.Principal;

@Slf4j
@Named("principalBean")
@SessionScoped
public class PrincipalBean implements Serializable {

    private static final long serialVersionUID = 2048901109636132224L;

    @Inject
    private transient Principal principal;

    @Inject
    private PrincipalStatelessBean principalStatelessBean;

    private Employer employer;

    @Produces
    public Employer getLoggedUser() {
        if (employer == null) {
            refresh();
        }
        return employer;
    }

    private void refresh() {
        if (principal == null || "anonymous".equals(principal.getName())) {
            return;
        }
        try {
            employer = principalStatelessBean.getEmployerByEmail(principal.getName());
        } catch (Exception e) {
            log.error("Error fetching user", e);
        }
    }
}