package com.carpenter.core.control.repository;

import com.carpenter.core.control.service.login.PrincipalBean;
import com.carpenter.core.entity.Company;
import com.carpenter.core.entity.Company_;
import com.carpenter.core.entity.EmployeeGroup;
import com.carpenter.core.entity.dictionaries.Role;
import com.carpenter.core.entity.employee.Employee;
import com.carpenter.core.entity.employee.Employee_;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.jpa.QueryHints;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
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
                    "SELECT e FROM Employee e LEFT JOIN FETCH e.company LEFT JOIN FETCH e.employeeGroup WHERE e.id =:employeeId", Employee.class)
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

    public Collection<Employee> findAllActiveEmployeesByLoggedUser(PrincipalBean principalBean) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Employee> query = builder.createQuery(Employee.class);
        Root<Employee> root = query.from(Employee.class);

        Predicate defaultPredicate = defaultPredicate(builder, root, principalBean);
        Predicate predicate = builder.and(
                builder.isNull(root.get(Employee_.deleteDate)),
                builder.isNull(root.get(Employee_.deletedBy)),
                builder.isNotNull(root.get(Employee_.accountActive))
        );

        if (defaultPredicate != null) {
            predicate = builder.and(predicate, defaultPredicate);
        }

        query.where(predicate);
        try {
            return entityManager.createQuery(query)
                    .getResultList();
        } catch (NoResultException e) {
            return Collections.emptyList();
        }
    }

    private Predicate defaultPredicate(CriteriaBuilder builder, Root<Employee> root, PrincipalBean principalBean) {
        Predicate predicate;
        if (principalBean.getLoggedUser().isInRole(Role.ADMINISTRATOR.name())) {
            predicate = null;
        } else if (principalBean.getLoggedUser().isInRole(Role.MANAGER.name())) {
            predicate = builder.equal(root.get(Employee_.company).get(Company_.id), principalBean.getLoggedUser().getCompany().getId());
        } else {
            predicate = builder.and(
                    builder.equal(root.get(Employee_.company).get(Company_.id), principalBean.getLoggedUser().getCompany().getId()),
                    builder.equal(root.get(Employee_.id), principalBean.getLoggedUser().getId())
            );
        }
        return predicate;
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
