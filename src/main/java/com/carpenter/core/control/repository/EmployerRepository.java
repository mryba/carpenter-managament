package com.carpenter.core.control.repository;

import com.carpenter.core.entity.employee.Employer;

import javax.ejb.Stateful;
import javax.enterprise.context.SessionScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;

@SessionScoped
@Stateful
public class EmployerRepository implements Serializable {

    private static final long serialVersionUID = -5269304361401704028L;
    @PersistenceContext
    private transient EntityManager entityManager;

    public Employer getEmployerByEmail(String email) {
        return entityManager.createNamedQuery("Employee.findEmployerByEmail", Employer.class)
                .setParameter("email", email).getSingleResult();

    }
}
