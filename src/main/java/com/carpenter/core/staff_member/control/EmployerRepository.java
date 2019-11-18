package com.carpenter.core.staff_member.control;

import com.carpenter.core.staff_member.entity.Employee;

import javax.ejb.Stateful;
import javax.enterprise.context.SessionScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;

@SessionScoped
@Stateful
 class EmployerRepository implements Serializable {

    private static final long serialVersionUID = -5269304361401704028L;
    @PersistenceContext
    private transient EntityManager entityManager;

    public Employee getEmployerByEmail(String email) {
        return entityManager.createNamedQuery("Employee.findEmployerByEmail", Employee.class)
                .setParameter("email", email).getSingleResult();

    }
}
