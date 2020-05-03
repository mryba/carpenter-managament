package com.carpenter.core.control.service.employee;

import com.carpenter.core.control.repository.EmployerRepository;
import com.carpenter.core.entity.employee.Employer;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;

@Named(value = "employerBean")
@Stateless
public class EmployerBean {

    @Inject
    private EmployerRepository employerRepository;

    public Employer getEmployerByEmail(String email) {
        return employerRepository.getEmployerByEmail(email);
    }
}
