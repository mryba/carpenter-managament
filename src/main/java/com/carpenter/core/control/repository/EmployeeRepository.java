package com.carpenter.core.control.repository;

import com.carpenter.core.entity.EmployeeGroup;
import com.carpenter.core.entity.employee.Employee;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.jpa.QueryHints;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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
            final String EMPLOYEE_ID = "employeeId";
            Employee employee = entityManager.createQuery(
                    "SELECT e FROM Employee e LEFT JOIN FETCH e.addresses WHERE e.id =:employeeId", Employee.class)
                    .setParameter(EMPLOYEE_ID, employeeId)
                    .setHint(QueryHints.HINT_PASS_DISTINCT_THROUGH, false)
                    .getResultList().iterator().next();

            employee = entityManager.createQuery(
                    "SELECT e FROM Employee e LEFT JOIN FETCH e.workingDays WHERE e.id=:employeeId", Employee.class)
                    .setParameter(EMPLOYEE_ID, employee.getId())
                    .setHint(QueryHints.HINT_PASS_DISTINCT_THROUGH, false)
                    .getResultList().iterator().next();

            employee = entityManager.createQuery(
                    "SELECT e FROM Employee e LEFT JOIN FETCH e.invoices WHERE e.id=:employeeId", Employee.class)
                    .setParameter(EMPLOYEE_ID, employee.getId())
                    .setHint(QueryHints.HINT_PASS_DISTINCT_THROUGH, false)
                    .getResultList().iterator().next();

            employee = entityManager.createQuery(
                    "SELECT e FROM Employee e LEFT JOIN FETCH e.company WHERE e.id =:employeeId", Employee.class)
                    .setParameter(EMPLOYEE_ID, employee.getId())
                    .setHint(QueryHints.HINT_PASS_DISTINCT_THROUGH, false)
                    .getResultList().iterator().next();
            return employee;
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

    public List<Employee> findAllSelfEmployment() {
        try {
            List<Employee> employees = entityManager.createQuery("SELECT e FROM Employee e LEFT JOIN FETCH e.addresses WHERE e.deletedBy IS NULL AND e.deleteDate is NULL AND e.contract ='SELF_EMPLOYMENT' AND e.accountActive = TRUE", Employee.class)
                    .setHint(QueryHints.HINT_PASS_DISTINCT_THROUGH, false)
                    .getResultList();
            List<Long> employeeIds = employees.stream().map(Employee::getId).collect(Collectors.toList());


            employees = entityManager.createQuery("SELECT e FROM Employee e LEFT JOIN FETCH e.workingDays WHERE e.id IN :employees", Employee.class)
                    .setParameter("employees", employeeIds)
                    .setHint(QueryHints.HINT_PASS_DISTINCT_THROUGH, false)
                    .getResultList();

            employeeIds = employees.stream().map(Employee::getId).collect(Collectors.toList());

            employees = entityManager.createQuery("SELECT e FROM Employee e LEFT JOIN FETCH e.company WHERE e.id IN :employees", Employee.class)
                    .setParameter("employees", employeeIds)
                    .setHint(QueryHints.HINT_PASS_DISTINCT_THROUGH, false)
                    .getResultList();

            return employees;
        } catch (NoResultException e) {
            return Collections.emptyList();
        }
    }

    public List<Employee> findAllEmployeeByIds(List<Long> ids) {
        try {
            List<Employee> employees = entityManager.createQuery("SELECT e FROM Employee e LEFT JOIN FETCH e.addresses WHERE e.id IN :ids ", Employee.class)
                    .setHint(QueryHints.HINT_PASS_DISTINCT_THROUGH, false)
                    .setParameter("ids", ids)
                    .getResultList();
            List<Long> employeeIds = employees.stream().map(Employee::getId).collect(Collectors.toList());


            employees = entityManager.createQuery("SELECT e FROM Employee e LEFT JOIN FETCH e.workingDays WHERE e.id IN :employees", Employee.class)
                    .setParameter("employees", employeeIds)
                    .setHint(QueryHints.HINT_PASS_DISTINCT_THROUGH, false)
                    .getResultList();

            employeeIds = employees.stream().map(Employee::getId).collect(Collectors.toList());

            employees = entityManager.createQuery("SELECT e FROM Employee e LEFT JOIN FETCH e.company WHERE e.id IN :employees", Employee.class)
                    .setParameter("employees", employeeIds)
                    .setHint(QueryHints.HINT_PASS_DISTINCT_THROUGH, false)
                    .getResultList();

            return employees;
        } catch (NoResultException e) {
            return Collections.emptyList();
        }
    }

    public List<Employee> findAllActiveAndWithoutGroupEmployees() {
        try {
            List<Employee> employees = entityManager.createQuery(
                    "SELECT e FROM Employee e LEFT JOIN FETCH e.addresses WHERE e.deletedBy IS NULL AND e.deleteDate is NULL AND e.accountActive = TRUE AND e.employeeGroup IS NULL ", Employee.class)
                    .setHint(QueryHints.HINT_PASS_DISTINCT_THROUGH, false)
                    .getResultList();
            List<Long> employeeIds = employees.stream().map(Employee::getId).collect(Collectors.toList());

            if (!employeeIds.isEmpty()) {
                employees = entityManager.createQuery("SELECT e FROM Employee e LEFT JOIN FETCH e.workingDays WHERE e.id IN :employees", Employee.class)
                        .setParameter("employees", employeeIds)
                        .setHint(QueryHints.HINT_PASS_DISTINCT_THROUGH, false)
                        .getResultList();
            }
            employeeIds = employees.stream().map(Employee::getId).collect(Collectors.toList());

            if (!employeeIds.isEmpty()) {
                employees = entityManager.createQuery("SELECT e FROM Employee e LEFT JOIN FETCH e.company WHERE e.id IN :employees", Employee.class)
                        .setParameter("employees", employeeIds)
                        .setHint(QueryHints.HINT_PASS_DISTINCT_THROUGH, false)
                        .getResultList();
            }
            return employees;
        } catch (NoResultException e) {
            return Collections.emptyList();
        }
    }

    public Collection<Employee> findAllActiveEmployeesByEmployeeGroup(EmployeeGroup employeeGroup) {
        try {
            List<Employee> employees = entityManager.createQuery(
                    "SELECT e FROM Employee e LEFT JOIN FETCH e.addresses WHERE e.deletedBy IS NULL AND e.deleteDate is NULL AND e.accountActive = TRUE AND e.employeeGroup IS NOT NULL AND e.employeeGroup =:employeeGroup", Employee.class)
                    .setParameter("employeeGroup", employeeGroup)
                    .setHint(QueryHints.HINT_PASS_DISTINCT_THROUGH, false)
                    .getResultList();
            List<Long> employeeIds = employees.stream().map(Employee::getId).collect(Collectors.toList());

            if (!employeeIds.isEmpty()) {
                employees = entityManager.createQuery("SELECT e FROM Employee e LEFT JOIN FETCH e.workingDays WHERE e.id IN :employees", Employee.class)
                        .setParameter("employees", employeeIds)
                        .setHint(QueryHints.HINT_PASS_DISTINCT_THROUGH, false)
                        .getResultList();
            }
            employeeIds = employees.stream().map(Employee::getId).collect(Collectors.toList());

            if (!employeeIds.isEmpty()) {
                employees = entityManager.createQuery("SELECT e FROM Employee e LEFT JOIN FETCH e.company WHERE e.id IN :employees", Employee.class)
                        .setParameter("employees", employeeIds)
                        .setHint(QueryHints.HINT_PASS_DISTINCT_THROUGH, false)
                        .getResultList();
            }
            return employees;
        } catch (NoResultException e) {
            return Collections.emptyList();
        }
    }
}
