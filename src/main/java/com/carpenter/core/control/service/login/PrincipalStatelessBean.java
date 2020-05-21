package com.carpenter.core.control.service.login;

import com.carpenter.core.entity.employee.Employee;
import lombok.extern.slf4j.Slf4j;

import javax.ejb.Stateless;
import javax.persistence.*;

import java.io.Serializable;

@Slf4j
@Stateless
public class PrincipalStatelessBean implements Serializable {

    private static final long serialVersionUID = 4105220738992463830L;

    @PersistenceContext
    private EntityManager entityManager;

    public Employee getEmployerByEmail(String email) {
        try {
            Employee employee = entityManager.createNamedQuery("Employee.findEmployerByEmail", Employee.class)
                    .setParameter("email", email)
                    .getResultList().iterator().next();
            log.debug("Fetched user {}", employee);
            return employee;
        } catch (NonUniqueResultException | NoResultException e) {
            log.error("Cannot fetch user with email: {}", email);
            return null;
        }
    }
}
