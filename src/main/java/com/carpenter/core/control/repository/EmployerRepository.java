package com.carpenter.core.control.repository;

import com.carpenter.core.control.dto.EmployerDto;
import com.carpenter.core.entity.employee.Employer;
import lombok.extern.slf4j.Slf4j;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;

@Slf4j
@Stateless
public class EmployerRepository implements Serializable {

    private static final long serialVersionUID = -5269304361401704028L;

    @PersistenceContext
    private transient EntityManager entityManager;

    public Employer getEmployerByEmail(String email) {
        return entityManager.createNamedQuery("Employee.findEmployerByEmail", Employer.class)
                .setParameter("email", email).getResultList().stream().findFirst().orElse(null);

    }

    public List<Employer> findAllEmployers() {
        try {
            return entityManager.createQuery("SELECT e from Employer e LEFT JOIN FETCH e.company WHERE e.deleteDate is NULL", Employer.class)
                    .getResultList();
        } catch (NoResultException e) {
            log.error("No employers found!");
            return Collections.emptyList();
        }
    }

    public void saveEmployee(Employer employer) {
        entityManager.merge(employer);
    }

    public Long findEmployeeByEmail(String email) {
        try {
            return entityManager.createQuery("SELECT e.id FROM Employer e WHERE e.email =:email ", Long.class)
                    .setParameter("email", email)
                    .getResultList()
                    .stream().findFirst().orElse(0L);
        } catch (NonUniqueResultException e) {
            return 0L;
        }
    }
}
