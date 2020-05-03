package com.carpenter.core.control.service.employee;

import com.carpenter.core.entity.employee.Employer;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named("employerBean")
@ViewScoped
public class EmployerBean implements Serializable {

    private static final long serialVersionUID = 7312921022733891332L;

    @Inject
    EmployerService employerService;

    public List<Employer> getEmployersList() {
        return employerService.getEmployersList();
    }

    public void addEmployer(){

    }

}
