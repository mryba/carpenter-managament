package com.carpenter.core.control.repository;

import com.carpenter.core.entity.employee.Employee;
import lombok.extern.slf4j.Slf4j;

import javax.ejb.Stateless;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static com.carpenter.utils.ConstantsRegex.FETCH_GRAPH;

@Slf4j
@Stateless
public class EmployeeRepository implements Serializable {

    private static final long serialVersionUID = -5269304361401704028L;

    @PersistenceContext
    private transient EntityManager entityManager;

    public Employee getEmployeeByEmail(String email) {
        return entityManager.createNamedQuery("Employee.findEmployerByEmail", Employee.class)
                .setParameter("email", email).getSingleResult();

    }

    public List<Employee> findAllEmployees() {
        try {
            return entityManager.createQuery("SELECT e from Employee e LEFT JOIN FETCH e.company LEFT JOIN FETCH e.addresses WHERE e.deleteDate is NULL", Employee.class)
                    .getResultList();
        } catch (NoResultException e) {
            log.error("No employers found!");
            return Collections.emptyList();
        }
    }

    public void saveEmployee(Employee employee) {
        entityManager.merge(employee);
    }

    public Long findEmployeeByEmail(String email) {
        try {
            return entityManager.createQuery("SELECT e.id FROM Employee e WHERE e.email =:email ", Long.class)
                    .setParameter("email", email)
                    .getResultList()
                    .stream().findFirst().orElse(0L);
        } catch (NonUniqueResultException e) {
            return 0L;
        }
    }

    public Employee findEmployeeBeId(Long employeeId) {
        try {
            EntityGraph<?> graph = entityManager.getEntityGraph("Employee.fetch");
            return entityManager.createNamedQuery("Employee.findEmployeeById", Employee.class)
                    .setParameter("employeeId", employeeId)
                    .setHint(FETCH_GRAPH, graph)
                    .getResultList().iterator().next();
        } catch (NonUniqueResultException e) {
            return null;
        }
    }

    public Collection<Employee> findAllActiveEmployees() {
        try {
            return entityManager.createNamedQuery("Employee.findAllActiveEmployee", Employee.class)
                    .getResultList();
        } catch (NoResultException e) {
            return Collections.emptyList();
        }
    }
}
