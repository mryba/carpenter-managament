package com.carpenter.core.control.service.employee;

import com.carpenter.core.control.dto.EmployeeDto;
import com.carpenter.core.entity.employee.Employee;
import org.omnifaces.cdi.Param;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

@ViewScoped
@Named("employeeBean")
public class EmployeeBean implements Serializable {

    private static final long serialVersionUID = 5412921022733891332L;

    @Inject
    @Param(name = "employeeId")
    private Long employeeId;

    private EmployeeDto employeeDto;

    private EmployeeMapper employeeMapper;

    @Inject
    EmployeeService employeeService;

    @PostConstruct
    public void init() {
        if (employeeId != null) {
            employeeMapper = new EmployeeMapper();
            Employee employee = employeeService.getEmployeeById(employeeId);
            employeeDto = employeeMapper.mapToDomain(employee);
        }
    }

    public EmployeeDto getEmployeeDto() {
        return employeeDto;
    }
}
