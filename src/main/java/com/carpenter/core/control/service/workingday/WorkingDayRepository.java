package com.carpenter.core.control.service.workingday;

import com.carpenter.core.entity.WorkingDay;
import com.carpenter.core.entity.WorkingDay_;
import com.carpenter.core.entity.employee.Employee;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collection;
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

    public void mergeWorkingDay(WorkingDay workingDay) {
        entityManager.merge(workingDay);
    }

    public List<WorkingDay> findAllWorkingDaysInScope(Date startDate, Date endDate) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<WorkingDay> query = builder.createQuery(WorkingDay.class);
        Root<WorkingDay> root = query.from(WorkingDay.class);
        root.fetch(WorkingDay_.employee, JoinType.LEFT);

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

    public WorkingDay findEmployeeWorkDayById(Long id, LocalDate day) {
        try {
            Date dayDate = Date.from(day.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
            return entityManager.createNamedQuery("WorkingDay.findEmployeeWorkDay", WorkingDay.class)
                    .setParameter("employeeId", id)
                    .setParameter("day", dayDate)
                    .getResultList().stream().findFirst().orElse(null);
        } catch (NonUniqueResultException e) {
            return null;
        }
    }

    public Collection<WorkingDay> findAllWorkingDayByDate(LocalDate yesterday) {
        try {
            Date yesterdayDate = Date.from(yesterday.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
            return entityManager.createNamedQuery("WorkingDay.findAllWorkingDayByDate", WorkingDay.class)
                    .setParameter("yesterday", yesterdayDate)
                    .getResultList();
        } catch (NoResultException e) {
            return Collections.emptyList();
        }
    }
}
