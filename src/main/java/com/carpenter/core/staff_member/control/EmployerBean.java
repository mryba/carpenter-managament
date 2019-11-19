package com.carpenter.core.staff_member.control;

import com.carpenter.core.staff_member.entity.Employer;

import javax.inject.Named;

@Named(value = "employerBean")
public class EmployerBean {

    private EmployerRepository employerRepository;

    public Employer getEmployerByEmail(String email) {
        return employerRepository.getEmployerByEmail(email);
    }
}
