package com.carpenter.core.control.service.workingday;

import com.carpenter.core.control.repository.EmployeeRepository;
import com.carpenter.core.entity.WorkingDay;
import com.carpenter.core.entity.employee.Employee;
import lombok.extern.slf4j.Slf4j;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Singleton
@Startup
public class WorkingDayScheduler implements Serializable {

    @Inject
    WorkingDayRepository workingDayRepository;

    @Inject
    EmployeeRepository employeeRepository;

    @Schedule(minute = "30", hour = "23", persistent = false)
    public void initWorkingDay() {
        LocalDate yesterday = LocalDate.now().minusDays(2);

        if (yesterday.getDayOfWeek() == DayOfWeek.SATURDAY || yesterday.getDayOfWeek() == DayOfWeek.SUNDAY) {
            log.info("It's day off ({} - {}) - no work date time added.", yesterday, yesterday.getDayOfWeek());
            return;
        }

        Collection<Employee> allActiveEmployees = employeeRepository.findAllActiveEmployees();
        Collection<WorkingDay> allYesterdayWorkingDay = workingDayRepository.findAllWorkingDayByDate(yesterday);

        if (allYesterdayWorkingDay == null || allYesterdayWorkingDay.isEmpty()) {
            allActiveEmployees.forEach(e -> initWorkingDay(e, yesterday));
            log.info("Create working day for all active employees: {}", allActiveEmployees.stream().map(Employee::getId).collect(Collectors.toList()));
        }

        if (allYesterdayWorkingDay != null && !allYesterdayWorkingDay.isEmpty()) {
            Set<Employee> toAdd = new LinkedHashSet<>();

            allYesterdayWorkingDay.stream()
                    .filter(wd -> allActiveEmployees.contains(wd.getEmployee()))
                    .forEach(wd -> toAdd.add(wd.getEmployee()));

            allActiveEmployees.removeIf(toAdd::contains);

            allActiveEmployees.forEach(e -> initWorkingDay(e, yesterday));
            log.info("Create working day for employees: {}", toAdd);
        }
    }

    private void initWorkingDay(Employee employee, LocalDate yesterday) {
        WorkingDay workingDay = new WorkingDay();
        workingDay.setHours(8);
        workingDay.setCreateDate(new Date());
        workingDay.setCreateBy("SYSTEM");
        workingDay.setDay(Date.from(yesterday.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
        workingDay.setEmployee(employee);
        workingDayRepository.saveWorkingDay(workingDay);
    }
}
