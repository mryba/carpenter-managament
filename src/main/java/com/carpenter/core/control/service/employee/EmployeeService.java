package com.carpenter.core.control.service.employee;

import com.carpenter.core.control.dto.EmployeeDto;
import com.carpenter.core.control.repository.EmployeeRepository;
import com.carpenter.core.control.service.login.PrincipalBean;
import com.carpenter.core.entity.EmployeeGroup;
import com.carpenter.core.entity.employee.Employee;
import lombok.extern.slf4j.Slf4j;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.*;

@Slf4j
@Stateless
public class EmployeeService implements Serializable {

    private static final long serialVersionUID = 5285699775473333310L;

    @Inject
    private EmployeeRepository employeeRepository;

    private EmployeeMapper employeeMapper;

    public List<Employee> getEmployersList() {
        return employeeRepository.findAllEmployees();
    }

    public Employee createEmployee(EmployeeDto employeeDto, PrincipalBean principalBean) {
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

    public List<Employee> getAllActiveSelfEmploymentEmployees() {
        return employeeRepository.findAllSelfEmployment();
    }

    public List<EmployeeDto> getAllActiveEmployees(List<Employee> employees) {
        List<EmployeeDto> employeesList = new LinkedList<>();
        employeeMapper = new EmployeeMapper();
        for (Employee employee : employees) {
            EmployeeDto employeeDto = employeeMapper.mapToDomain(employee);
            employeesList.add(employeeDto);
        }
        return employeesList;
    }

    public List<EmployeeDto> getAllActiveEmployees() {
        List<EmployeeDto> employees = new LinkedList<>();
        employeeMapper = new EmployeeMapper();
        Collection<Employee> allSelfEmployment = employeeRepository.findAllActiveEmployees();
        for (Employee employee : allSelfEmployment) {
            EmployeeDto employeeDto = employeeMapper.mapToDomain(employee);
            employees.add(employeeDto);
        }
        return employees;
    }

    public List<EmployeeDto> getAllActiveEmployeesByGroup(EmployeeGroup employeeGroup) {
        List<EmployeeDto> employeesDto = new LinkedList<>();
        Collection<Employee> employees;
        employeeMapper = new EmployeeMapper();

        if (employeeGroup == null) {
            employees = employeeRepository.findAllActiveAndWithoutGroupEmployees();
        } else {
            employees = employeeRepository.findAllActiveEmployeesByEmployeeGroup(employeeGroup);

        }
        for (Employee employee : employees) {
            EmployeeDto employeeDto = employeeMapper.mapToDomain(employee);
            employeesDto.add(employeeDto);
        }
        return employeesDto;
    }

    public List<Employee> getAllEmployeesByIds(List<Long> employeeIds) {
        return employeeRepository.findAllEmployeeByIds(employeeIds);
    }

    public List<EmployeeDto> getAllActiveAndWithoutGroupEmployees() {
        List<EmployeeDto> employees = new ArrayList<>();
        employeeMapper = new EmployeeMapper();
        List<Employee> notSignedEmployees = employeeRepository.findAllActiveAndWithoutGroupEmployees();
        for (Employee notSignedEmployee : notSignedEmployees) {
            EmployeeDto employeeDto = employeeMapper.mapToDomain(notSignedEmployee);
            employees.add(employeeDto);
        }
        return employees;
    }
}