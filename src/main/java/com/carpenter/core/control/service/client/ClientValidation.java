package com.carpenter.core.control.service.client;

import com.carpenter.utils.ConstantsRegex;
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

    public void validatePostalCode(FacesContext facesContext, UIComponent component, Object value) {
        validateEmptyField( value, "com.carpenter.notNull");
        String postalCode = (String) value;
        if (!(postalCode).matches(ConstantsRegex.POSTAL_CODE_PATTERN)) {
            throw new ValidatorException(
                    new FacesMessage(
                            resourceBundle.getString("employee.postal.code"),
                            resourceBundle.getString("employee.postal.code")
                    )
            );
        }
    }

    public void validateName(FacesContext facesContext, UIComponent component, Object value) {
        validateEmptyField(value, "client.name.notNull");
    }

    public void validateNip(FacesContext facesContext, UIComponent component, Object value) {
        validateEmptyField(value, "client.nip.notNull");
        if (!InputValidator.isNipValid(value.toString())) {
            invalidInput("com.carpenter.nip.invalid", "com.carpenter.nip.invalid");
        }
        if (!clientService.isClientNew((String) value)) {
            invalidInput("client.nip.exists", "client.nip.exists");
        }
    }

    public void validateEmail(FacesContext facesContext, UIComponent component, Object value) {
        validateEmptyField(value, "com.carpenter.email.notNull");
        if (!InputValidator.isEmailValid(value.toString())) {
            invalidInput("com.carpenter.email.invalid", "com.carpenter.email.invalid");
        }
    }

    public void validatePhoneNumber(FacesContext facesContext, UIComponent component, Object value) {
        validateEmptyField(value, "com.carpenter.phone.notNull");
    }

    public void validateBankAccountNumber(FacesContext facesContext, UIComponent component, Object value) {
        if (!InputValidator.isBankAccountNumberValid(value.toString())) {
            invalidInput(
                    "com.carpenter.bankAccountNumber.invalid",
                    "com.carpenter.bankAccountNumber.invalid");
        }
    }

    private void validateEmptyField(Object value, String summary) {
        if (value == null || value.toString().isEmpty()) {
            invalidInput(summary, "com.carpenter.notNull");
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
