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
        validateEmptyField(value, "client.name.notNull", "com.carpenter.notNull");
    }

    public void validateNip(FacesContext facesContext, UIComponent component, Object value) {
        validateEmptyField(value, "client.nip.notNull", "com.carpenter.notNull");
        if (!InputValidator.isNipValid(value.toString())) {
            invalidInput("com.carpenter.nip.invalid", "com.carpenter.nip.invalid");
        }
    }

    public void validateEmail(FacesContext facesContext, UIComponent component, Object value) {
        validateEmptyField(value, "com.carpenter.email.notNull", "com.carpenter.notNull");
        if (!InputValidator.isEmailValid(value.toString())) {
            invalidInput("com.carpenter.email.invalid", "com.carpenter.email.invalid");
        }
    }

    public void validatePhoneNumber(FacesContext facesContext, UIComponent component, Object value) {
        validateEmptyField(value, "com.carpenter.phone.notNull", "com.carpenter.notNull");
    }

    public void validateBankAccountNumber(FacesContext facesContext, UIComponent component, Object value) {
        if (!InputValidator.isBankAccountNumberValid(value.toString())) {
            invalidInput(
                    "com.carpenter.bankAccountNumber.invalid",
                    "com.carpenter.bankAccountNumber.invalid");
        }
    }

    private void validateEmptyField(Object value, String summary, String detail) {
        if (value == null || value.toString().isEmpty()) {
            invalidInput(summary, detail);
        }
    }

    private void invalidInput(String summary, String detail) {
        throw new ValidatorException(
                new FacesMessage(
                        resourceBundle.getString(summary),
                        resourceBundle.getString(detail)
                )
        );
    }

}
