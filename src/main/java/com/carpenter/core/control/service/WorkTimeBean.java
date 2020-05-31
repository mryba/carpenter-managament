package com.carpenter.core.control.service;

import com.carpenter.core.control.dto.EmployeeDto;
import com.carpenter.core.control.service.employee.EmployeeService;
import com.carpenter.core.entity.dictionaries.Day;
import lombok.Getter;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZoneId;
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

    @Inject
    EmployeeService employeeService;

    @PostConstruct
    public void init() {
        employees = employeeService.getEmployees();
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
}
