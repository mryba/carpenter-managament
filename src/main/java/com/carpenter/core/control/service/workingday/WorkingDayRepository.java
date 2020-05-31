package com.carpenter.core.control.service.workingday;

import com.carpenter.core.entity.WorkingDay;
import com.carpenter.core.entity.WorkingDay_;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Stateless
public class WorkingDayRepository implements Serializable {

    @PersistenceContext
    private EntityManager entityManager;

    public void saveWorkingDay(WorkingDay workingDay) {
        entityManager.persist(workingDay);
    }

    public List<WorkingDay> findAllWorkingDaysInScope(Date startDate, Date endDate) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<WorkingDay> query = builder.createQuery(WorkingDay.class);
        Root<WorkingDay> root = query.from(WorkingDay.class);

        Predicate predicate = builder.and(
                builder.greaterThanOrEqualTo(root.get(WorkingDay_.day), startDate),
                builder.lessThanOrEqualTo(root.get(WorkingDay_.day), endDate)
        );

        query.where(predicate);

        try {
            return entityManager.createQuery(query)
                    .getResultList();
        } catch (NoResultException e) {
            return Collections.emptyList();
        }
    }
}
