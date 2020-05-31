package com.carpenter.core.control.service.calendar;

import com.carpenter.core.control.dto.EmployeeDto;
import com.carpenter.core.control.service.employee.EmployeeMapper;
import com.carpenter.core.control.service.workingday.WorkingDayService;
import com.carpenter.core.entity.WorkingDay;
import com.carpenter.core.entity.dictionaries.Day;
import com.carpenter.core.entity.dictionaries.Week;
import com.carpenter.core.entity.employee.Employee;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Named("calendarWeekBean")
@ViewScoped
@Slf4j
public class CalendarWeekBean extends CalendarBean {

    private static final long serialVersionUID = 7818636965206530503L;

    private List<EmployeeDto> employeeDtos = new LinkedList<>();
    private Map<Week, List<WorkingDay>> workingDaysMap = new LinkedHashMap<>();

    @Inject
    WorkingDayService workingDayService;

    @PostConstruct
    public void initWeekCalendar() {
        initCalendarMode(Mode.WEEK);
    }

    public List<Date> getDates() {
        List<WorkingDay> workingWeek = workingDayService.getWorkingWeek(timeManager.getStartDate(), timeManager.getEndDate());

        List<Date> dates = new LinkedList<>();
        LocalDateTime startDate = timeManager.getViewStartDate();
        int count = 0;

        while (startDate.isBefore(timeManager.getViewEndDate())) {
            Date from = Date.from(startDate.atZone(ZoneId.systemDefault()).toInstant());
            dates.add(from);

            final LocalDate startDateAsLocalDate = startDate.toLocalDate();
            List<WorkingDay> day = workingWeek.stream().filter(d -> d.getDay().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().equals(startDateAsLocalDate)).collect(Collectors.toList());
            startDate = startDate.plusDays(1);

            final Integer staticCount = count;
            Week week = Stream.of(Week.values()).filter(w -> w.getNumber().equals(staticCount)).findFirst().orElse(null);
            if (week != null) {
                workingDaysMap.put(week, day);
            }
            count++;
        }
        return dates;
    }

    public List<EmployeeDto> getEmployees() {
        employeeDtos.clear();
        employeeDtos.addAll(employeeService.getEmployees());
        return employeeDtos;
    }

    public Integer getGetWorkDays(String type, EmployeeDto employee) {
        List<WorkingDay> workingDays = workingDaysMap.get(Week.valueOf(type));
        WorkingDay workingDay = workingDays.stream().filter(d -> d.getEmployee().getId().equals(employee.getId())).findFirst().orElse(null);
        if (workingDay != null) {
            return workingDay.getHours();
        }
        return 0;
    }
}
