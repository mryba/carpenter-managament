package com.carpenter.core.control.utils.logger;

import com.carpenter.core.control.service.login.CrmPrincipal;
import com.carpenter.core.control.service.login.ExtendedPrincipal;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import java.io.Serializable;
import java.security.Principal;
import java.util.Arrays;

@Slf4j
@Logged
@Interceptor
@Priority(Interceptor.Priority.APPLICATION)
public class LoggingInterceptor implements Serializable {

    private static final long serialVersionUID = -6524662080734441584L;

    @Inject
    Principal principal;

    @AroundInvoke
    public Object logMethodEntry(InvocationContext invocationContext) throws Exception {
        long timestamp = System.nanoTime();
        boolean simpleGetter = false;

        String method = invocationContext.getMethod().getName();
        if ((method.startsWith("get") || method.startsWith("is")) || method.startsWith("should") && invocationContext.getMethod().getParameterCount() == 0) {
            simpleGetter = true;
        }

        try {
            MDC.put("ctx", invocationContext.getTarget().getClass().getSimpleName() + "#" + invocationContext.getMethod().getName());

            if (simpleGetter) {
                log.debug(">> ({}) Entering {}.{}", principal.getName(), invocationContext.getMethod().getDeclaringClass().getCanonicalName(), method);
            } else {
                log.info(">> ({}) Entering {}.{}", principal.getName(), invocationContext.getMethod().getDeclaringClass().getCanonicalName(), method);
            }

            if (log.isDebugEnabled() && !(method.startsWith("get") || method.startsWith("is"))) {
                log.debug(">>       Parameters: {}", Arrays.toString(invocationContext.getParameters()));
            }
            return invocationContext.proceed();
        } finally {
            if (simpleGetter) {
                log.debug("<< ({}) Finished {}.{} in {} us", principal.getName(), invocationContext.getMethod().getDeclaringClass().getCanonicalName(), method, (System.nanoTime() - timestamp) / 1000);

            } else {
                log.info("<< ({}) Finished {}.{} in {} us", principal.getName(), invocationContext.getMethod().getDeclaringClass().getCanonicalName(), method, (System.nanoTime() - timestamp) / 1000);
            }
            MDC.remove("ctx");
        }

    }
}
