package com.carpenter.core.control.service.workingday;

import com.carpenter.core.entity.WorkingDay;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import java.io.Serializable;

@SessionScoped
public class WorkingDayService implements Serializable {

    @Inject
    private WorkingDayRepository workingDayRepository;

    public void saveWorkingDay(WorkingDay workingDay){
        workingDayRepository.saveWorkingDay(workingDay);
    }
}
