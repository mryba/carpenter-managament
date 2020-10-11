package com.carpenter.core.control.service.workingday;

import com.carpenter.core.control.service.login.PrincipalBean;
import com.carpenter.core.entity.WorkingDay;

import javax.ejb.Stateless;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Stateless
public class WorkingDayService implements Serializable {

    private static final long serialVersionUID = -2762901092997937651L;

    @Inject
    private WorkingDayRepository workingDayRepository;

    public void saveWorkingDay(WorkingDay workingDay){
        workingDayRepository.saveWorkingDay(workingDay);
    }

    public void mergeWorkingDay(WorkingDay workingDay){
        workingDayRepository.mergeWorkingDay(workingDay);
    }

    public List<WorkingDay> getWorkingDaysInScope(Date startDate, Date endDate, PrincipalBean principalBean) {
        return workingDayRepository.findAllWorkingDaysInScope(startDate, endDate, principalBean);
    }

    public WorkingDay findIfEmployeeWorkDayIsPerform(Long id, LocalDate day) {
        return workingDayRepository.findEmployeeWorkDayById(id, day);
    }

}
