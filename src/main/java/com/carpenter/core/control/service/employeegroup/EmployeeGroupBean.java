package com.carpenter.core.control.service.employeegroup;

import com.carpenter.core.control.dto.EmployeeDto;
import com.carpenter.core.control.service.employee.EmployeeService;
import com.carpenter.core.control.service.login.PrincipalBean;
import com.carpenter.core.entity.EmployeeGroup;
import com.carpenter.core.entity.employee.Employee;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Slf4j
@Named("employeeGroupBean")
@ViewScoped
public class EmployeeGroupBean implements Serializable {

    private static final long serialVersionUID = -7314112007094597293L;

    private EmployeeGroup employeeGroup;

    private List<Long> employeeIds;
    private List<EmployeeGroup> employeeGroups;

    @Inject
    EmployeeService employeeService;

    @Inject
    EmployeeGroupService employeeGroupService;

    @Inject
    PrincipalBean principalBean;

    @PostConstruct
    public void init() {
        employeeGroup = new EmployeeGroup();
        employeeIds = new ArrayList<>();
        employeeGroups = employeeGroupService.getAllEmployeeGroups();
    }

    public List<EmployeeDto> getAllActiveEmployees() {
        return employeeService.getAllActiveEmployees();
    }

    public void setEditedEmployeeGroup(Long employeeGroupId) {
        if (employeeGroups != null && !employeeGroups.isEmpty()) {
            employeeGroup = employeeGroups.stream().filter(eg -> eg.getId().equals(employeeGroupId)).findFirst().orElse(null);
        }
    }

    public void deleteGroup(Long employeeGroupId) {
        if (employeeGroups != null && !employeeGroups.isEmpty()) {
            employeeGroup = employeeGroups.stream().filter(e -> e.getId().equals(employeeGroupId)).findFirst().orElse(null);

            for (Employee employee : employeeGroup.getEmployees()) {
                employeeGroup.removeEmployee(employee);
                employeeGroupService.deleteGroup(employeeGroup);
            }
        }
    }

    public void save() {
        List<Employee> employees = employeeService.getAllEmployeesByIds(employeeIds);
        employees.forEach(e -> employeeGroup.addEmployee(e));
        employeeGroup.setCreateBy(principalBean.getLoggedUser().getEmail());
        employeeGroup.setCreateDate(new Date());
        employeeGroupService.saveGroup(employeeGroup);
    }
}
