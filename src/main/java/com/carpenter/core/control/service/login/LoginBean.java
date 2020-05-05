package com.carpenter.core.control.service.login;

import com.carpenter.core.control.repository.EmployerRepository;
import com.carpenter.core.entity.employee.Employer;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;

@Named(value = "loginBean")
@Stateless
public class LoginBean {

    @Inject
    private EmployerRepository employerRepository;

    public Employer getEmployerByEmail(String email) {
        return employerRepository.getEmployerByEmail(email);
    }
}
