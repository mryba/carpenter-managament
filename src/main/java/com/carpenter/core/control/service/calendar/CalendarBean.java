package com.carpenter.core.control.service.calendar;

import com.carpenter.core.control.dto.EmployeeDto;
import com.carpenter.core.control.service.employee.EmployeeMapper;
import com.carpenter.core.control.service.employee.EmployeeService;
import com.carpenter.core.control.service.login.PrincipalBean;
import com.carpenter.core.control.service.workingday.WorkingDayService;
import lombok.Getter;
import lombok.Setter;

import javax.inject.Inject;
import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public abstract class CalendarBean implements Serializable {

    private static final long serialVersionUID = 4437366168733136L;

    @Inject
    CalendarTimeManager timeManager;

    @Inject
    PrincipalBean principalBean;

    @Inject
    EmployeeService employeeService;

    @Inject
    WorkingDayService workingDayService;

    EmployeeMapper employeeMapper;

    private final List<EmployeeDto> employeeDtos = new LinkedList<>();


    public void initCalendarMode(Mode mode) {
        timeManager.setMode(mode);
    }

    public void moveForward() {
        timeManager.moveForward();
    }

    public void moveBackward() {
        timeManager.moveBackward();
    }

    public List<EmployeeDto> getEmployees() {
        employeeDtos.clear();
        employeeDtos.addAll(employeeService.getAllActiveEmployees(principalBean));
        return employeeDtos.stream().sorted(Comparator.comparing(EmployeeDto::getLastName)).collect(Collectors.toList());
    }


    public boolean isWeekend(LocalDate date) {
        return date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY;
    }

    public boolean isWeekend(Date date) {
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return localDate.getDayOfWeek() == DayOfWeek.SATURDAY || localDate.getDayOfWeek() == DayOfWeek.SUNDAY;
    }
}
