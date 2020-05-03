package com.carpenter.core.control.service.login;

import com.carpenter.core.entity.employee.Employer;
import lombok.extern.slf4j.Slf4j;

import javax.ejb.Stateless;
import javax.persistence.*;

import java.io.Serializable;

import static com.carpenter.utils.ConstantsRegex.FETCH_GRAPH;

@Slf4j
@Stateless
public class PrincipalStatelessBean implements Serializable {

    private static final long serialVersionUID = 4105220738992463830L;

    @PersistenceContext
    private EntityManager entityManager;

    public Employer getEmployerByEmail(String email) {
        try {
            Employer employer = entityManager.createNamedQuery("Employee.findEmployerByEmail", Employer.class)
                    .setParameter("email", email)
                    .getResultList().iterator().next();
            log.debug("Fetched user {}", employer);
            return employer;
        } catch (NonUniqueResultException | NoResultException e) {
            log.error("Cannot fetch user with email: {}", email);
            return null;
        }
    }
}
