package com.carpenter.core.control.service.naviagation;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

@Named("navigationBean")
@ApplicationScoped
public class NavigationBean {

    public String employeesRedirect() {
        return "/secure/employees/employees?faces-redirect=true";
    }

    public String employeeRedirect() {
        return "/secure/employees/employee?faces-redirect=true";
    }

    public String clientsRedirect() {
        return "/secure/clients/clients?faces-redirect=true";
    }

    public String offersRedirect() {
        return "/secure/offers/offers?faces-redirect=true";
    }

    public String adminRedirect() {
        return "/secure/admin/admin-dashboard?faces-redirect=true";
    }

    public String calendarDayViewRedirect() {
        return "/secure/calendars/week-calendar.xhtml?faces-redirect=true";
    }

    public String calendarMonthViewRedirect() {
        return "/secure/calendars/month-calendar.xhtml?faces-redirect=true";
    }

    public String invoiceRedirect() {
        return "/secure/invoice/invoices?faces-redirect=true";
    }
}
