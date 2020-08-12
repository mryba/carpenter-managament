package com.carpenter.core.control.repository;

import com.carpenter.core.control.dto.EmployeeDto;
import com.carpenter.core.control.service.login.PrincipalBean;
import com.carpenter.core.entity.Company;
import com.carpenter.core.entity.Company_;
import com.carpenter.core.entity.dictionaries.Contract;
import com.carpenter.core.entity.dictionaries.Role;
import com.carpenter.core.entity.employee.Employee;
import com.carpenter.core.entity.employee.Employee_;
import lombok.extern.slf4j.Slf4j;

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
import java.util.Set;

@Slf4j
@Stateless
public class EmployeeRepository implements Serializable {

    private static final long serialVersionUID = -5269304361401704028L;

    @PersistenceContext
    private transient EntityManager entityManager;

    public List<EmployeeDto> getActiveAndSelfEmploymentEmployees(PrincipalBean principalBean) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<EmployeeDto> query = builder.createQuery(EmployeeDto.class);
        Root<Employee> root = query.from(Employee.class);

        Predicate predicate = getDefaultPredicate(builder, root, principalBean);
        Predicate employeePredicate =
                builder.and(
                        builder.or(
                                builder.isTrue(root.get(Employee_.accountActive)),
                                builder.isNotNull(root.get(Employee_.accountActive))
                        ),
                        builder.equal(root.get(Employee_.contract), Contract.SELF_EMPLOYMENT)
                );
        predicate = builder.and(predicate, employeePredicate);

        query.where(predicate);

        query.select(builder.construct(
                EmployeeDto.class,
                ));

    }

    private Predicate getDefaultPredicate(CriteriaBuilder builder, Root<Employee> root, PrincipalBean principalBean) {
        Predicate predicate = null;
        if (!principalBean.getLoggedUser().isInRole(Role.ADMINISTRATOR.name())) {
            predicate = builder.equal(root.get(Employee_.company).get(Company_.id), principalBean.getLoggedUser().getCompany().getId());
        }
        return predicate;
    }

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

    public List<Employee> findAllSelfEmploymentEmployees() {
        try {
            return entityManager.createNamedQuery("Employee.findAllSelfEmploymentEmployees", Employee.class)
                    .getResultList();
        } catch (NoResultException e) {
            log.error("No self employment employees found");
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
            Employee employee = entityManager.createQuery(
                    "SELECT e FROM Employee e LEFT JOIN FETCH e.addresses WHERE e.id =:employeeId", Employee.class)
                    .setParameter("employeeId", employeeId)
                    .getResultList().iterator().next();

            employee = entityManager.createQuery(
                    "SELECT e FROM Employee e LEFT JOIN FETCH e.workingDays WHERE e.id=:employeeId", Employee.class)
                    .setParameter("employeeId", employeeId)
                    .getResultList().iterator().next();

            employee = entityManager.createQuery(
                    "SELECT e FROM Employee e LEFT JOIN FETCH e.invoices WHERE e.id=:employeeId", Employee.class)
                    .setParameter("employeeId", employeeId)
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
}
