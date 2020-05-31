package com.carpenter.core.control.service.workingday;

import com.carpenter.core.entity.WorkingDay;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@SessionScoped
public class WorkingDayService implements Serializable {

    @Inject
    private WorkingDayRepository workingDayRepository;

    public void saveWorkingDay(WorkingDay workingDay){
        workingDayRepository.saveWorkingDay(workingDay);
    }

    public List<WorkingDay> getWorkingWeek(Date startDate, Date endDate) {
        return workingDayRepository.findAllWorkingDaysInScope(startDate, endDate);
    }
}
