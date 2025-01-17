package com.carpenter.core.control.service.workingday;

import com.carpenter.core.control.dto.EmployeeDto;
import com.carpenter.core.control.service.client.ClientService;
import com.carpenter.core.control.service.employee.EmployeeService;
import com.carpenter.core.control.service.employeegroup.EmployeeGroupService;
import com.carpenter.core.control.service.login.PrincipalBean;
import com.carpenter.core.entity.EmployeeGroup;
import com.carpenter.core.entity.WorkingDay;
import com.carpenter.core.entity.dictionaries.Day;
import com.carpenter.core.entity.employee.Employee;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Observes;
import javax.enterprise.event.Reception;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
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

@Getter
@Setter
@ViewScoped
@Named("workTimeBean")
public class WorkTimeBean implements Serializable {

    private static final long serialVersionUID = 1084861867586822994L;

    private LocalDate dateTime = LocalDate.now().minusDays(1);
    private boolean isBulkOperation = true;
    private Integer groupHours = Day.EIGHT.getNumber();

    private Map<EmployeeDto, Integer> employeesHours = new LinkedHashMap<>();
    @Getter
    private Map<EmployeeDto, Boolean> errorMessage = new LinkedHashMap<>();

    @Getter
    private List<EmployeeDto> employees;

    @Getter
    private List<EmployeeGroup> employeeGroups;

    @Inject
    EmployeeService employeeService;

    @Inject
    EmployeeGroupService employeeGroupService;

    @Inject
    WorkingDayService workingDayService;

    @Inject
    ClientService clientService;

    @Inject
    PrincipalBean principalBean;

    @Inject
    WorkTimeListener workTimeListener;

    @PostConstruct
    public void init() {
        refreshEmployeeGroup(workTimeListener);
    }

    private void refreshEmployeeGroup(@Observes(notifyObserver = Reception.IF_EXISTS) WorkTimeListener workTimeListener) {
        this.workTimeListener = workTimeListener;
        employeesHours.clear();
        errorMessage.clear();

        employeeGroups = employeeGroupService.getAllEmployeeGroups();

        EmployeeGroup employeeGroup = selectedEmployeeGroup();
        employees = employeeService.getAllActiveEmployeesByGroup(employeeGroup, principalBean);

        for (EmployeeDto employee : employees) {
            Integer hour = workingDayService.getEmployeeWorkTime(employee, dateTime);
            if (hour == null) {
                employeesHours.put(employee, groupHours);
                isBulkOperation = true;

            } else {
                employeesHours.put(employee, hour);
                isBulkOperation = false;
                groupHours = 0;
                errorMessage.put(employee, Boolean.TRUE);
            }
        }
    }

    private EmployeeGroup selectedEmployeeGroup() {
        return employeeGroups.stream().filter(eg -> eg.getId().equals(workTimeListener.getEmployeeGroupId())).findFirst().orElse(null);
    }

    public List<Day> getHour() {
        return Stream.of(Day.values()).collect(Collectors.toList());
    }

    public LocalDate getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDate dateTime) {
        this.dateTime = dateTime;
        refreshEmployeeGroup(workTimeListener);
    }

    public Integer getGroupHours() {
        return groupHours;
    }

    public void setGroupHours(Integer groupHours) {
        this.groupHours = groupHours;
        isBulkOperation = true;

        for (Map.Entry<EmployeeDto, Integer> entry : employeesHours.entrySet()) {
            entry.setValue(groupHours);
        }
    }

    public Integer employeeHour(EmployeeDto employee) {
        return employeesHours.get(employee);
    }

    public Integer getEmployeeHoursIfExists(EmployeeDto employee) {
        WorkingDay workDay = workingDayService.findIfEmployeeWorkDayIsPerform(employee.getId(), dateTime);
        return workDay != null ? workDay.getHours() : 0;
    }

    public void plusHour(EmployeeDto employee) {
        Integer hour = employeesHours.get(employee);
        if (hour < 24) {
            employeesHours.put(employee, hour + 1);
            isBulkOperation = false;
            groupHours = 0;
        }
    }

    public void minusHour(EmployeeDto employee) {
        Integer hour = employeesHours.get(employee);
        if (hour > 0) {
            employeesHours.put(employee, hour - 1);
            isBulkOperation = false;
            groupHours = 0;
        }
    }

    public void clear() {
        workTimeListener.setEmployeeGroupId(null);
        dateTime = LocalDate.now().minusDays(1);
        employeesHours.clear();
        groupHours = Day.EIGHT.getNumber();
        refreshEmployeeGroup(workTimeListener);
    }

    public void saveTime() {
        for (EmployeeDto employeeDto : employees) {

            WorkingDay workingDay = workingDayService.findIfEmployeeWorkDayIsPerform(employeeDto.getId(), dateTime);

            if (workingDay != null) {
                if (!isBulkOperation) {
                    workingDay.setHours(employeesHours.get(employeeDto));
                } else {
                    workingDay.setHours(groupHours);
                }
                if (workingDay.getHours() == null) {
                    workingDay.setHours(0);
                }
                workingDay.setEditDate(new Date());
                workingDayService.mergeWorkingDay(workingDay);
            } else {
                workingDay = new WorkingDay();
                workingDay.setCreateBy(principalBean.getLoggedUser().getEmail());
                workingDay.setCreateDate(new Date());
                workingDay.setDay(Date.from(dateTime.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
                workingDay.setCreateBy(principalBean.getLoggedUser().getEmail());

                Employee employee = employeeService.getEmployeeById(employeeDto.getId());

                if (!isBulkOperation) {
                    workingDay.setHours(employeesHours.get(employeeDto));
                } else {
                    workingDay.setHours(groupHours);
                }
                if (workingDay.getHours() == null) {
                    workingDay.setHours(0);
                }
                employee.addWorkingDay(workingDay);

                workingDayService.saveWorkingDay(workingDay);
            }
        }
        clear();
        refreshEmployeeGroup(workTimeListener);
    }

    public void validateDate(FacesContext facesContext, UIComponent component, Object value) {
        errorMessage.clear();
        if (value != null && !value.toString().isEmpty())
            for (EmployeeDto employee : employees) {
                LocalDate date = (LocalDate) value;
                WorkingDay workDay = workingDayService.findIfEmployeeWorkDayIsPerform(employee.getId(), date);

                if (workDay != null) {
                    errorMessage.put(employee, Boolean.TRUE);
                }
            }
    }

    public Integer getZero() {
        return 0;
    }
}

