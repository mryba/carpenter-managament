package com.carpenter.core.staff_member.control;

import com.carpenter.core.staff_member.entity.Employee;

import javax.inject.Named;

@Named(value = "employerBean")
public class EmployerBean {

    private EmployerRepository employerRepository;

    public Employee getEmployerByEmail(String email) {
        return employerRepository.getEmployerByEmail(email);
    }
}
