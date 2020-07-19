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
import java.util.LinkedList;
import java.util.List;

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
        employeeDtos.addAll(employeeService.getEmployees());
        return employeeDtos;
    }
}
