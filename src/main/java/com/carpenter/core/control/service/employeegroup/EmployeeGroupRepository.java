package com.carpenter.core.control.service.employeegroup;

import com.carpenter.core.entity.EmployeeGroup;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.jpa.QueryHints;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;

@Slf4j
@Stateless
public class EmployeeGroupRepository implements Serializable {

    private static final long serialVersionUID = 898047272514883917L;

    @PersistenceContext
    private EntityManager entityManager;


    public List<EmployeeGroup> findAllEmployeeGroups() {
        try {
            return entityManager.createQuery("SELECT DISTINCT eg FROM EmployeeGroup eg LEFT JOIN FETCH eg.employees WHERE eg.deleteDate IS NULL AND eg.deletedBy IS NULL", EmployeeGroup.class)
                    .setHint(QueryHints.HINT_PASS_DISTINCT_THROUGH, false)
                    .getResultList();
        } catch (NoResultException e) {
            return Collections.emptyList();
        }
    }

    public void save(EmployeeGroup employeeGroup) {
        entityManager.merge(employeeGroup);
    }

    public void removeGroup(EmployeeGroup employeeGroup) {
        entityManager.remove(entityManager.contains(employeeGroup) ? employeeGroup : entityManager.merge(employeeGroup));
    }
}
