package com.carpenter.core.control.service.naviagation;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

@Named("navigationBean")
@ApplicationScoped
public class NavigationBean {

    public String employersRedirect(){
        return "/secure/employees/employees?faces-redirect=true";
    }

    public String getEmployeeRedirect() {
        return "/secure/employees/employee?faces-redirect=true";
    }
}
