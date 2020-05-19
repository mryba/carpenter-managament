package com.carpenter.core.control.service.login;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ResourceBundle;

@ViewScoped
@Named("loginValidator")
public class LoginValidator implements Serializable {

    private static final long serialVersionUID = -4434593200640697639L;

    private ResourceBundle resourceBundle = ResourceBundle.getBundle("LoginMessages");

    public void validateEmail(FacesContext context, UIComponent component, Object value) {
        validateEmptyField(context, component, value, "email_empty");
    }

    public void validatePassword(FacesContext context, UIComponent component, Object value) {
        validateEmptyField(context, component, value, "password_empty");
    }

    private void validateEmptyField(FacesContext context, UIComponent component, Object value, String message) {
        if (value == null || value.toString().isEmpty()) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_WARN,
                    resourceBundle.getString(message),
                    resourceBundle.getString(message)
            ));
        }
    }
}
