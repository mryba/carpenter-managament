package com.carpenter.core.control.service.login;

import com.carpenter.core.control.utils.logger.Logged;
import com.carpenter.core.entity.employee.Employer;

import javax.annotation.PostConstruct;
import javax.faces.application.ConfigurableNavigationHandler;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIInput;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ResourceBundle;

@Logged
@Named
@ViewScoped
public class LoginController implements Serializable {

    private static final long serialVersionUID = -2154593200640697639L;

    @NotNull
    private String email;
    @NotNull
    private String password;
    private String originalUrl;

    @Inject
    private LoginBean loginBean;

    private transient UIInput errorComponent;

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UIInput getErrorComponent() {
        return errorComponent;
    }

    public void setErrorComponent(UIInput errorComponent) {
        this.errorComponent = errorComponent;
    }

    @PostConstruct
    public void computeOriginalUrl() {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        this.originalUrl = (String) externalContext.getRequestMap().get(RequestDispatcher.FORWARD_REQUEST_URI);

        if (this.originalUrl == null || originalUrl.isEmpty() || (originalUrl.length() == 1)) {
            //test
            originalUrl = "/secure/main.xhtml";
        }

        String originalQuery = (String) externalContext.getRequestMap().get(RequestDispatcher.FORWARD_QUERY_STRING);
        if (originalQuery != null) {
            originalUrl += "?" + originalQuery + "&faces-redirect=true";
        } else {
            originalUrl += "?faces-redirect=true";
        }
    }

    public void isLogged() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        String remoteUser = facesContext.getExternalContext().getRemoteUser();

        if (remoteUser != null) {
            ConfigurableNavigationHandler navigationHandler = (ConfigurableNavigationHandler) facesContext.getApplication().getNavigationHandler();
            navigationHandler.performNavigation("/secure/main.xhtml");
        }
    }

    public String login() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();

        HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();

        try {
            request.login(email, password);
            request.changeSessionId();
            Employer employer = loginBean.getEmployerByEmail(email);

            externalContext.getSessionMap().put("employer", employer);

            Map<String, Object> httpOnlyProperty = Collections.singletonMap("httpOnly", Boolean.TRUE);
            Map<String, Object> pathProperty = Collections.singletonMap("path", "/");

            Map<String, Object> cookieProperty = new LinkedHashMap<>();
            cookieProperty.putAll(httpOnlyProperty);
            cookieProperty.putAll(pathProperty);

            externalContext.addResponseCookie("stm-session-id", request.getSession(false).getId(),
                    Collections.singletonMap("httpOnly", Boolean.TRUE)
            );

            return originalUrl;

        } catch (ServletException e) {
            e.printStackTrace();
            errorComponent.setValid(false);
            ResourceBundle resourceBundle = ResourceBundle.getBundle("LoginMessages", FacesContext.getCurrentInstance().getViewRoot().getLocale());
            String login_error = resourceBundle.getString("login_error");

            facesContext.addMessage(errorComponent.getClientId(),
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, login_error, login_error));
        }
        return null;
    }

    public String logout() {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();

        externalContext.invalidateSession();
        return "/domain/login?faces-redirect=true";
    }
}
