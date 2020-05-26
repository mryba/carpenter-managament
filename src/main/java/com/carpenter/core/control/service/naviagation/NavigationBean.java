package com.carpenter.core.control.service.naviagation;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

@Named("navigationBean")
@ApplicationScoped
public class NavigationBean {

    public String employersRedirect(){
        return "/secure/employees/employees?faces-redirect=true";
    }

    public String employeeRedirect() {
        return "/secure/employees/employee?faces-redirect=true";
    }

    public String clientsRedirect() {
        return "/secure/clients/clients?faces-redirect=true";
    }

    public String offersRedirect(){
        return "/secure/offers/offers?faces-redirect=true";
    }

    public String calendarDayViewRedirect() {
        return "/secure/calendars/week-calendar.xhtml?faces-redirect=true";
    }
}
