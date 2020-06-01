package com.carpenter.core.control.service.workingday;

import com.carpenter.core.control.dto.EmployeeDto;
import com.carpenter.core.control.service.client.ClientService;
import com.carpenter.core.control.service.employee.EmployeeService;
import com.carpenter.core.control.service.login.PrincipalBean;
import com.carpenter.core.entity.WorkingDay;
import com.carpenter.core.entity.client.Client;
import com.carpenter.core.entity.dictionaries.Day;
import com.carpenter.core.entity.employee.Employee;
import lombok.Getter;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ViewScoped
@Named("workTimeBean")
public class WorkTimeBean implements Serializable {

    private Long clientId;
    private Date dateTime = new Date();
    private Integer groupHours = Day.EIGHT.getNumber();

    private Map<EmployeeDto, Integer> employeesHours = new LinkedHashMap<>();
    @Getter
    private List<EmployeeDto> employees;

    @Getter
    private List<Client> clients;

    @Inject
    EmployeeService employeeService;

    @Inject
    ClientService clientService;

    @Inject
    WorkingDayService workingDayService;

    @Inject
    PrincipalBean principalBean;

    @PostConstruct
    public void init() {
        employees = employeeService.getEmployees();
        clients = clientService.getClientsList();
        for (EmployeeDto employee : employees) {
            employeesHours.put(employee, groupHours);
        }
    }

    public List<Day> getHour() {
        return Stream.of(Day.values()).collect(Collectors.toList());
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public Integer getGroupHours() {
        return groupHours;
    }

    public void setGroupHours(Integer groupHours) {
        this.groupHours = groupHours;
    }

    public Integer employeeHour(EmployeeDto employee) {
        return employeesHours.get(employee);
    }

    public void plusHour(EmployeeDto employee) {
        Integer hour = employeesHours.get(employee);
        if (hour < 24) {
            employeesHours.put(employee, hour + 1);
        }
    }

    public void minusHour(EmployeeDto employee) {
        Integer hour = employeesHours.get(employee);
        if (hour > 0) {
            employeesHours.put(employee, hour - 1);
        }
    }

    public void clear() {
        clientId = null;
        dateTime = new Date();
        employeesHours.clear();
        groupHours = Day.EIGHT.getNumber();
        for (EmployeeDto employee : employees) {
            employeesHours.put(employee, groupHours);
        }
    }

    public void saveTime() {
        for (EmployeeDto employeeDto : employees) {
            WorkingDay workingDay = new WorkingDay();
            workingDay.setCreateDate(new Date());
            workingDay.setDay(dateTime);
            workingDay.setCreateBy(principalBean.getLoggedUser().getEmail());
            Employee employee = employeeService.getEmployeeById(employeeDto.getId());
            Client client = clients.stream().filter(c -> c.getId().equals(clientId)).findFirst().orElse(null);

            workingDay.setHours(employeesHours.get(employeeDto));
            employee.addWorkingDay(workingDay);
            if (client != null) {
                client.addWorkingDat(workingDay);
            }

            workingDayService.saveWorkingDay(workingDay);
        }
    }
}
