package com.carpenter.core.control.service.client;

import com.carpenter.utils.InputValidator;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ResourceBundle;

@ViewScoped
@Named("clientValidation")
public class ClientValidation implements Serializable {
    private final static long serialVersionUID = 4912365403594665L;

    private ResourceBundle resourceBundle = ResourceBundle.getBundle("ValidationMessages");

    @Inject
    ClientService clientService;

    public void validateName(FacesContext facesContext, UIComponent component, Object value) {
        validateEmptyField(facesContext, component, value, "com.carpenter.notNull");
    }

    public void validateNip(FacesContext facesContext, UIComponent component, Object value) {
        validateEmptyField(facesContext, component, value, "com.carpenter.notNull");
        if (!InputValidator.isNipValid(value.toString())) {
            throw new ValidatorException(new FacesMessage(
                    "invalid nip", "check sum incorrect"
            ));
        }
    }

    public void validateEmail(FacesContext facesContext, UIComponent component, Object value) {
        validateEmptyField(facesContext, component, value, "com.carpenter.notNull");
        if (!InputValidator.isEmailValid(value.toString())) {
            throw new ValidatorException(new FacesMessage(
                    "invalid email", "invalid email"
            ));
        }
    }

    public void validatePhoneNumber(FacesContext facesContext, UIComponent component, Object value) {
        validateEmptyField(facesContext, component, value, "com.carpenter.notNull");
    }

    public void validateBankAccountNumber(FacesContext facesContext, UIComponent component, Object value) {
        if (!InputValidator.isBankAccountNumberValid(value.toString())) {
            throw new ValidatorException(new FacesMessage(
                    "invalid bankAccount", "invalid bankAccount"
            ));
        }
    }

    public void validateEmptyField(FacesContext facesContext, UIComponent component, Object value, String message) {
        if (value == null || value.toString().isEmpty()) {
            throw new ValidatorException(new FacesMessage(
                    resourceBundle.getString(message),
                    resourceBundle.getString(message)
            ));
        }
    }


}
