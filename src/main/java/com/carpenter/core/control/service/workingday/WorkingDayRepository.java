package com.carpenter.core.control.service.workingday;

import com.carpenter.core.entity.WorkingDay;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;

@Stateless
public class WorkingDayRepository implements Serializable {

    @PersistenceContext
    private EntityManager entityManager;

    public void saveWorkingDay(WorkingDay workingDay) {
        entityManager.persist(workingDay);
    }
}
