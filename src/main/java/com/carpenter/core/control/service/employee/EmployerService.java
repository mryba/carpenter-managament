package com.carpenter.core.control.service.employee;

import com.carpenter.core.control.dto.EmployerDto;
import com.carpenter.core.control.repository.EmployerRepository;
import com.carpenter.core.entity.employee.Employer;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.List;

@Slf4j
@SessionScoped
public class EmployerService implements Serializable {

    private static final long serialVersionUID = 5285699775473333310L;

    @Inject
    EmployerRepository employerRepository;

    private EmployeeMapper employeeMapper;

    public List<Employer> getEmployersList() {
        return employerRepository.findAllEmployers();
    }

    public Employer createEmployee(EmployerDto employerDto) {
        employeeMapper = new EmployeeMapper();
        return employeeMapper.mapFromDomain(employerDto);
    }

    public void saveEmployee(Employer employee) {
        employerRepository.saveEmployee(employee);
    }

    public boolean getEmployeeByEmail(String email) {
        Long employeeByEmail = employerRepository.findEmployeeByEmail(email);
        return employeeByEmail > 0;
    }
}
