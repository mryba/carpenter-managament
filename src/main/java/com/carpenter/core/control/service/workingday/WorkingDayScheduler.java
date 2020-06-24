package com.carpenter.core.control.service.workingday;

import com.carpenter.core.control.repository.EmployeeRepository;
import com.carpenter.core.entity.WorkingDay;
import com.carpenter.core.entity.employee.Employee;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

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

        Collection<Employee> allActiveEmployees = employeeRepository.findAllActiveEmployees();

        Collection<WorkingDay> allYesterdayWorkingDay = workingDayRepository.findAllWorkingDayByDate(yesterday);

        Set<Employee> employeeWithoutWorkDay = allYesterdayWorkingDay.stream()
                .filter(w -> !allActiveEmployees.contains(w.getEmployee()))
                .map(WorkingDay::getEmployee)
                .collect(Collectors.toSet());

        employeeWithoutWorkDay.forEach(e->{
            WorkingDay workingDay = new WorkingDay();
            workingDay.setHours(8);
            workingDay.setCreateBy("SYSTEM");
            workingDay.setDay(Date.from(yesterday.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
            workingDay.setEmployee(e);
//            workingDay.setClient(); //Todo add to Client column that will be target the present client work or seperate this on group.
            workingDayRepository.saveWorkingDay(workingDay);
        });
    }
}
