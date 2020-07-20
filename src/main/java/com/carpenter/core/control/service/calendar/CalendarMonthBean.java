package com.carpenter.core.control.service.calendar;

import com.carpenter.core.control.dto.EmployeeDto;
import com.carpenter.core.entity.WorkingDay;
import com.carpenter.core.entity.employee.Employee;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.TextStyle;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Slf4j
@Named("calendarMonthBean")
@ViewScoped
public class CalendarMonthBean extends CalendarBean {

    private static final long serialVersionUID = 7818636965206530503L;

    private List<WorkingDay> workingWeek = new LinkedList<>();
    private Map<LocalDate, AtomicInteger> dateMap = new LinkedHashMap<>();
    private Map<LocalDate, RowRepresentative> employeeMap = new LinkedHashMap<>();

    @PostConstruct

    public void init() {
        workingWeek.clear();
        initCalendarMode(Mode.MONTH);
    }

    public String getCurrentMonthName() {
        return timeManager.getViewStartDate().getMonth()
                .getDisplayName(TextStyle.FULL_STANDALONE, FacesContext.getCurrentInstance().getExternalContext().getRequestLocale());
    }

    public List<Date> getDates() {
        List<Date> result = new LinkedList<>();

        LocalDateTime startDate = timeManager.getViewStartDate();
        LocalDateTime viewEndDate = timeManager.getViewEndDate();

        workingWeek = workingDayService.getWorkingDaysInScope(timeManager.getStartDate(), timeManager.getEndDate());

        while (startDate.isBefore(viewEndDate)) {
            Date from = Date.from(startDate.atZone(ZoneId.systemDefault()).toInstant());
            result.add(from);

            final LocalDate finalStartDate = startDate.toLocalDate();

            int sum = workingWeek.stream()
                    .filter(wd -> convertDateToLocalDate(wd.getDay()).equals(finalStartDate))
                    .mapToInt(WorkingDay::getHours).sum();
            dateMap.computeIfAbsent(finalStartDate, k -> new AtomicInteger(sum));

            for (WorkingDay workingDay : workingWeek) {
                if (convertDateToLocalDate(workingDay.getDay()).equals(finalStartDate) && !employeeMap.containsKey(finalStartDate)) {
                    employeeMap.computeIfAbsent(finalStartDate, k -> new RowRepresentative(workingDay.getEmployee().getId(), new AtomicInteger(workingDay.getHours())));
                }
            }

            startDate = startDate.plusDays(1);
        }
        return result;
    }

    public Integer getWorkHours(Date date, EmployeeDto employeeDto) {
        LocalDate dateAsLocalDate = convertDateToLocalDate(date);
        return workingWeek.stream()
                .filter(w -> w.getEmployee().getId().equals(employeeDto.getId()))
                .filter(wd -> convertDateToLocalDate(wd.getDay()).equals(dateAsLocalDate))
                .map(WorkingDay::getHours)
                .findFirst().orElse(0);
    }

    private static LocalDate convertDateToLocalDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public Integer getCountEmployeeHour(Date date) {
        LocalDate localDate = convertDateToLocalDate(date);
        AtomicInteger atomicInteger = dateMap.get(localDate);
        if (atomicInteger != null) {
            return atomicInteger.get();
        }
        return 0;
    }

    public Integer getSumOfColumns() {
        return dateMap.values().stream().mapToInt(AtomicInteger::get).sum();
    }

    public Integer getRowCount(EmployeeDto employeeDto) {
        List<RowRepresentative> rr = employeeMap.values().stream().filter(r -> r.getEmployeeId().equals(employeeDto.getId())).collect(Collectors.toList());
        if (!rr.isEmpty()) {
            return rr.stream().mapToInt(rowRepresentative -> rowRepresentative.getHours().get()).sum();
        }
        return 0;
    }
}
