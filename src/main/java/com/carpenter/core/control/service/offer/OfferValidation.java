package com.carpenter.core.control.service.offer;

import lombok.Getter;
import lombok.Setter;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ResourceBundle;

@ViewScoped
@Named("offerValidation")
public class OfferValidation implements Serializable {

    private ResourceBundle resourceBundle = ResourceBundle.getBundle("ValidationMessages");

    @Getter
    @Setter
    @Size(max = 9, message = "Pole ")
    private String phone;

    public void validateWorkCity(FacesContext facesContext, UIComponent component, Object value) {
        validateEmptyField(facesContext, component, value, "offer.workCity.notNull");
    }

    public void validateWorkDateFrom(FacesContext facesContext, UIComponent component, Object value) {
        validateEmptyField(facesContext, component, value, "offer.workDateFrom.notNull");
    }

    public void validateBuildingDimension(FacesContext facesContext, UIComponent component, Object value) {
        validateEmptyField(facesContext, component, value, "offer.buildingDimension.notNull");
    }

    public void validatePhone(FacesContext facesContext, UIComponent component, Object value) {
        validateEmptyField(facesContext, component, value, "offer.phone.notNull");
        phone = (String) value;
    }

    public void validateEmail(FacesContext facesContext, UIComponent component, Object value) {
        validateEmptyField(facesContext, component, value, "offer.email.notNull");
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
}
