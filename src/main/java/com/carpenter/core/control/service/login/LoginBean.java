package com.carpenter.core.control.service.login;

import com.carpenter.core.control.repository.EmployeeRepository;
import com.carpenter.core.entity.employee.Employee;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;

@Named(value = "loginBean")
@Stateless
public class LoginBean {

    @Inject
    private EmployeeRepository employeeRepository;

    public Employee getEmployeeByEmail(String email) {
        return employeeRepository.getEmployeeByEmail(email);
    }
}
