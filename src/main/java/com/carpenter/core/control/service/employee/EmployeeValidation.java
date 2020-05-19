package com.carpenter.core.control.service.employee;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.codec.digest.DigestUtils;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ResourceBundle;

@ViewScoped
@Named("employeeValidation")
public class EmployeeValidation implements Serializable {

    private static final long serialVersionUID = 8247119983863720299L;

    private ResourceBundle resourceBundle = ResourceBundle.getBundle("ValidationMessages");

    @Inject
    EmployeeService employeeService;

    private String password;
    private String rePassword;

    @Getter
    @Setter
    @Size(max = 9, message = "Pole ")
    private String phone;


    public void validateFirstPassword(FacesContext facesContext, UIComponent component, Object value) {
        validateEmptyField(facesContext, component, value, "com.carpenter.notNull");
    }

    public void validateRePassword(FacesContext facesContext, UIComponent component, Object value) {
        validateEmptyField(facesContext, component, value, "com.carpenter.notNull");
        rePassword = value.toString();

        if (!rePassword.equals(password)) {
            throw new ValidatorException(
                    new FacesMessage(
                            resourceBundle.getString("com.carpenter.password.equals"),
                            resourceBundle.getString("com.carpenter.password.equals")
                    )
            );
        }
    }

    public void validateForename(FacesContext facesContext, UIComponent component, Object value) {
        validateEmptyField(facesContext, component, value, "employee.forename.notNull");
    }

    public void validateLastName(FacesContext facesContext, UIComponent component, Object value) {
        validateEmptyField(facesContext, component, value, "employee.lastName.notNull");
    }

    public void validateEmail(FacesContext facesContext, UIComponent component, Object value) {
        validateEmptyField(facesContext, component, value, "employee.email.notNull");

        String email = (String) value;
        boolean isEmployeeWithThatEmailExists = employeeService.getEmployeeByEmail(email);
        if (isEmployeeWithThatEmailExists) {
            throw new ValidatorException(
                    new FacesMessage(
                            resourceBundle.getString("employee.email.exists"),
                            resourceBundle.getString("employee.email.exists")
                    )
            );
        }
    }

    public void validatePhone(FacesContext facesContext, UIComponent component, Object value) {
        validateEmptyField(facesContext, component, value, "employee.phone.notNull");
        phone = (String) value;
    }

    private void validateEmptyField(FacesContext facesContext, UIComponent component, Object value, String message) {
        if (value == null || value.toString().isEmpty()) {
            throw new ValidatorException(
                    new FacesMessage(
                            resourceBundle.getString(message),
                            resourceBundle.getString(message)
                    )
            );
        }
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRePassword() {
        return rePassword;
    }

    public void setRePassword(String rePassword) {
        this.rePassword = rePassword;
    }

    public String getHashedPassword() {
        if (password.equals(rePassword)) {
            return DigestUtils.sha256Hex(password);
        }
        return null;
    }

}
