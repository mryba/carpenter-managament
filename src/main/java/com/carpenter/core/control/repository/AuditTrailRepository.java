package com.carpenter.core.control.repository;

import com.carpenter.core.control.service.audit.AuditTrailFilters;
import com.carpenter.core.entity.AuditTrail;
import com.carpenter.core.entity.AuditTrail_;
import com.carpenter.core.entity.employee.Employee;
import com.carpenter.core.entity.employee.Employee_;

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
import java.util.List;

@Stateless
public class AuditTrailRepository implements Serializable {

    private static final long serialVersionUID = 2649121915071717997L;

    @PersistenceContext
    private EntityManager entityManager;

    public List<AuditTrail> findAuditsByFilter(AuditTrailFilters filters) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<AuditTrail> query = builder.createQuery(AuditTrail.class);
        Root<AuditTrail> root = query.from(AuditTrail.class);

        Predicate filterPredicate = getDefaultPredicate(builder, root, filters);
        if (filterPredicate != null) {
            query.where(filterPredicate);
        }
        try {
            return entityManager.createQuery(query).getResultList();
        } catch (NoResultException e) {
            return Collections.emptyList();
        }
    }

    private Predicate getDefaultPredicate(CriteriaBuilder builder, Root<AuditTrail> root, AuditTrailFilters filters) {
        Predicate predicate = null;
        if (filters != null) {
            if (filters.getEmployeeFilter() != null && !filters.getEmployeeFilter().isEmpty()) {
                predicate = builder.isTrue(root.get(AuditTrail_.employee).get(Employee_.id).in(filters.getEmployeeFilter()));
            }
        }
        return predicate;
    }

    public void save(AuditTrail auditTrail) {
        entityManager.persist(auditTrail);
    }
}
