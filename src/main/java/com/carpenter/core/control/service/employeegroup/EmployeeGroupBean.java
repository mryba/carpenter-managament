package com.carpenter.core.control.service.employeegroup;

import com.carpenter.core.control.dto.EmployeeDto;
import com.carpenter.core.control.service.employee.EmployeeService;
import com.carpenter.core.entity.EmployeeGroup;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Slf4j
@Named("employeeGroupBean")
@ViewScoped
public class EmployeeGroupBean implements Serializable {

    private static final long serialVersionUID = -7314112007094597293L;

    private EmployeeGroup employeeGroup;

    private List<Long> employeeGroupIds;

    @Inject
    EmployeeService employeeService;

    @PostConstruct
    public void init() {
        employeeGroup = new EmployeeGroup();
        employeeGroupIds = new ArrayList<>();
    }

    public List<EmployeeDto> getAllActiveEmployees() {
        return employeeService.getAllActiveEmployees();
    }

}
