package com.carpenter.core.control.service.employee;

import org.apache.commons.codec.cli.Digest;
import org.apache.commons.codec.digest.DigestUtils;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

@ViewScoped
@Named("employeeValidation")
public class EmployeeValidation implements Serializable {

    private static final long serialVersionUID = 8247119983863720299L;

    private static final String PASSWORD = "PASSWORD";
    private static final String RE_PASSWORD = "RE_PASSWORD";

    private ResourceBundle resourceBundle = ResourceBundle.getBundle("ValidationMessages");

    private String password;
    private String rePassword;

    private Map<String, Boolean> mapOfFields;

    @PostConstruct
    public void init() {
        mapOfFields = new HashMap<>();
        mapOfFields.put(PASSWORD, false);
        mapOfFields.put(RE_PASSWORD, false);
    }

    public void validateFirstPassword(FacesContext facesContext, UIComponent component, Object value) {
        if (value == null) {
            mapOfFields.put(PASSWORD, false);
            mapOfFields.put(RE_PASSWORD, false);

            throw new ValidatorException(
                    new FacesMessage(
                            resourceBundle.getString("com.carpenter.password.empty"),
                            resourceBundle.getString("com.carpenter.password.empty"))
            );
        }
    }

    public void validateRePassword(FacesContext facesContext, UIComponent component, Object value) {
        if (value == null) {
            throw new ValidatorException(
                    new FacesMessage(
                            resourceBundle.getString("com.carpenter.password.empty"),
                            resourceBundle.getString("com.carpenter.password.empty")
                    )
            );
        }
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
