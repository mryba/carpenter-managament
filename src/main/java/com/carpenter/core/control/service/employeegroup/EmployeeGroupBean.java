package com.carpenter.core.control.service.employeegroup;

import com.carpenter.core.control.dto.ClientDto;
import com.carpenter.core.control.dto.EmployeeDto;
import com.carpenter.core.control.service.client.ClientService;
import com.carpenter.core.control.service.employee.EmployeeService;
import com.carpenter.core.control.service.login.PrincipalBean;
import com.carpenter.core.entity.EmployeeGroup;
import com.carpenter.core.entity.client.Client;
import com.carpenter.core.entity.employee.Employee;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Getter
@Setter
@Slf4j
@Named("employeeGroupBean")
@ViewScoped
public class EmployeeGroupBean implements Serializable {

    private static final long serialVersionUID = -7314112007094597293L;

    private EmployeeGroup employeeGroup;
    private Long clientId;

    private List<Long> employeeIds;
    private List<EmployeeGroup> employeeGroups;

    @Inject
    EmployeeService employeeService;

    @Inject
    EmployeeGroupService employeeGroupService;

    @Inject
    PrincipalBean principalBean;

    @Inject
    private ClientService clientService;

    @PostConstruct
    public void init() {
        employeeGroup = new EmployeeGroup();
        employeeIds = new ArrayList<>();
        employeeGroups = employeeGroupService.getAllEmployeeGroups();
    }

    public List<EmployeeDto> getAllActiveEmployees() {
        return employeeService.getAllActiveAndWithoutGroupEmployees();
    }

    public void setEditedEmployeeGroup(Long employeeGroupId) {
        if (employeeGroups != null && !employeeGroups.isEmpty()) {
            employeeGroup = employeeGroups.stream().filter(eg -> eg.getId().equals(employeeGroupId)).findFirst().orElse(null);
        }
    }

    @Transactional
    public void deleteGroup() {
        if (employeeGroups != null && !employeeGroups.isEmpty()) {
            if (employeeGroup != null && employeeGroup.getEmployees() != null) {
                for (Employee employee : employeeGroup.getEmployees()) {
                    employee.setEmployeeGroup(null);
                    employeeService.saveEmployee(employee);
                }
                employeeGroup.setEmployees(null);
                employeeGroupService.deleteGroup(employeeGroup);
            }
        }
    }

    public void save() {
        List<Employee> employees = employeeService.getAllEmployeesByIds(employeeIds);
        Client client = clientService.getClientById(clientId);
        employees.forEach(e -> employeeGroup.addEmployee(e));
        employeeGroup.setCreateBy(principalBean.getLoggedUser().getEmail());
        employeeGroup.setCreateDate(new Date());
        employeeGroup.setPresentClient(client);
        employeeGroupService.saveGroup(employeeGroup);
    }

    public List<ClientDto> getAvailableClients() {
        return clientService.getAllAvailableClients();
    }

}
