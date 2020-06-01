package com.carpenter.core.control.service.calendar;

import com.carpenter.core.control.dto.EmployeeDto;
import com.carpenter.core.control.service.employee.EmployeeMapper;
import com.carpenter.core.control.service.workingday.WorkingDayService;
import com.carpenter.core.entity.WorkingDay;
import com.carpenter.core.entity.client.Client;
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

    private final List<EmployeeDto> employeeDtos = new LinkedList<>();
    private final Map<Week, List<WorkingDay>> workingDaysMap = new LinkedHashMap<>();
    private final Map<Date, Week> weekMap = new LinkedHashMap<>();
    private final Map<EmployeeDto, Integer> employeeDtoListMap = new LinkedHashMap<>();

    private int sumColumn = 0;

    @Inject
    WorkingDayService workingDayService;

    @PostConstruct
    public void initWeekCalendar() {
        initCalendarMode(Mode.WEEK);
    }

    public List<Date> getDates() {
        sumColumn = 0;
        List<WorkingDay> workingWeek = workingDayService.getWorkingWeek(timeManager.getStartDate(), timeManager.getEndDate());

        List<Date> dates = new LinkedList<>();
        LocalDateTime startDate = timeManager.getViewStartDate();
        int count = 0;

        while (startDate.isBefore(timeManager.getViewEndDate())) {
            Date from = Date.from(startDate.atZone(ZoneId.systemDefault()).toInstant());
            dates.add(from);

            final LocalDate startDateAsLocalDate = startDate.toLocalDate();
            List<WorkingDay> day = workingWeek
                    .stream()
                    .filter(d -> d.getDay().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().equals(startDateAsLocalDate))
                    .collect(Collectors.toList());

            startDate = startDate.plusDays(1);

            final Integer staticCount = count;
            Stream.of(Week.values()).filter(w -> w.getNumber().equals(staticCount)).findFirst().ifPresent(week -> {
                workingDaysMap.put(week, day);
                weekMap.put(from, week);
            });
            count++;
        }
        return dates;
    }

    public Integer getColumnCount(Date date) {
        Week week = weekMap.get(date);
        int columnCount = Math.toIntExact(workingDaysMap.get(week).stream().mapToInt(WorkingDay::getHours).sum());
        sumColumn = sumColumn + columnCount;
        return columnCount;
    }


    public Integer getGetRowCount(EmployeeDto employee) {
        return employeeDtoListMap.get(employee);
    }

    public int getSumColumn() {
        return sumColumn;
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

            Integer rowCount = employeeDtoListMap.computeIfAbsent(employee, r -> 0);
            rowCount = rowCount + workingDay.getHours();
            employeeDtoListMap.put(employee, rowCount);
            return workingDay.getHours();
        }
        return 0;
    }

}
