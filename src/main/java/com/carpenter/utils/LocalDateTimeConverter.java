package com.carpenter.utils;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@FacesConverter(value="localDateTimeConverter")
public class LocalDateTimeConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        return LocalDateTime.parse(value);
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        LocalDateTime dateValue = (LocalDateTime) value;
        return dateValue.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

}