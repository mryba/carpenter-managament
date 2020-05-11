package com.carpenter.utils;


import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter("country-code-converter")
public class CountryCodeSupplier implements Converter {

    private static final String COUNTRY_CODE = "+48";
    private static final String REGEX = "\\s";

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent component, String string) {
        if (string == null || string.isEmpty()) {
            return null;
        } else if (string.startsWith(COUNTRY_CODE)) {
            return string.replaceAll(REGEX, "");
        } else {
            return COUNTRY_CODE + string.replaceAll(REGEX, "");
        }
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
        if (!(object instanceof String)) {
            return null;
        }
        String string = (String) object;

        if (string.startsWith(COUNTRY_CODE)) {
            return string.substring(COUNTRY_CODE.length()).replaceAll(REGEX, "");
        } else {
            return string.replaceAll(REGEX, "");
        }
    }
}
