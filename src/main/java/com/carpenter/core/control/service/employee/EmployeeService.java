package com.carpenter.core.control.service.employee;

import com.carpenter.core.control.dto.EmployeeDto;
import com.carpenter.core.control.repository.EmployeeRepository;
import com.carpenter.core.control.service.login.PrincipalBean;
import com.carpenter.core.entity.employee.Employee;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Slf4j
@SessionScoped
public class EmployeeService implements Serializable {

    private static final long serialVersionUID = 5285699775473333310L;

    @Inject
    EmployeeRepository employeeRepository;

    @Inject
    PrincipalBean principalBean;

    private EmployeeMapper employeeMapper;

    public List<Employee> getEmployersList() {
        return employeeRepository.findAllEmployees();
    }

    public Employee createEmployee(EmployeeDto employeeDto) {
        employeeMapper = new EmployeeMapper();
        Employee employee = employeeMapper.mapFromDomain(employeeDto);
        employee.setCreateBy(principalBean.getLoggedUser().getEmail());
        employee.setCreateDate(new Date());
        return employee;
    }

    public void saveEmployee(Employee employee) {
        employeeRepository.saveEmployee(employee);
    }

    public boolean getEmployeeByEmail(String email) {
        Long employeeByEmail = employeeRepository.findEmployeeByEmail(email);
        return employeeByEmail > 0;
    }

    public Employee getEmployeeById(Long employeeId) {
        return employeeRepository.findEmployeeBeId(employeeId);
    }
}
