package com.carpenter.core.control.service.login;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.enterprise.inject.Produces;

@Stateless
public class PrincipalProducer {

    @Resource
    private SessionContext ctx;

    @Produces
    @CrmPrincipal
    public ExtendedPrincipal getCustomPrincipal() {
        try {
            return (ExtendedPrincipal) ctx.getCallerPrincipal();
        } catch (IllegalStateException e) {
            return null;
        }
    }
}