package com.carpenter.core.staff_member.control;

import com.carpenter.core.staff_member.entity.Employer;

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
