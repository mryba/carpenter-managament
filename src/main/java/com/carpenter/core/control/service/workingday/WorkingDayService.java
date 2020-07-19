package com.carpenter.core.control.service.workingday;

import com.carpenter.core.entity.WorkingDay;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@SessionScoped
public class WorkingDayService implements Serializable {

    @Inject
    private WorkingDayRepository workingDayRepository;

    public void saveWorkingDay(WorkingDay workingDay){
        workingDayRepository.saveWorkingDay(workingDay);
    }

    public void mergeWorkingDay(WorkingDay workingDay){
        workingDayRepository.mergeWorkingDay(workingDay);
    }

    public List<WorkingDay> getWorkingDaysInScope(Date startDate, Date endDate) {
        return workingDayRepository.findAllWorkingDaysInScope(startDate, endDate);
    }

    public WorkingDay findIfEmployeeWorkDayIsPerform(Long id, LocalDate day) {
        return workingDayRepository.findEmployeeWorkDayById(id, day);
    }

}
