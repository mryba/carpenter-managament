package com.carpenter.core.control.service.calendar;

import com.carpenter.core.control.dto.EmployeeDto;
import com.carpenter.core.entity.WorkingDay;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
@Setter
public class RecordRowRepresentative {

    private EmployeeDto employeeDto;
    private AtomicInteger hours;
    private List<LocalDate> monthlyDates;

    private final Map<LocalDate, AtomicInteger> hourMap = new LinkedHashMap<>();
    private int total;

    public RecordRowRepresentative(EmployeeDto employeeDto, AtomicInteger hours, List<LocalDate> monthlyDates) {
        this(monthlyDates);
        this.employeeDto = employeeDto;
        this.hours = hours;
    }

    public RecordRowRepresentative(List<LocalDate> monthlyDates) {
        this.monthlyDates = monthlyDates;
        for (LocalDate monthlyDate : monthlyDates) {
            hourMap.put(monthlyDate, new AtomicInteger(0));
        }
    }

    public void countDay(List<WorkingDay> workingDays) {
        for (WorkingDay workingDay : workingDays) {
            hourMap.keySet().stream().filter(hm -> hm.equals(convertDateToLocalDate(workingDay.getDay())))
                    .findFirst().ifPresent(date -> hourMap.put(date, new AtomicInteger(workingDay.getHours())));
        }
    }

    private static LocalDate convertDateToLocalDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }
}
