package com.carpenter.core.control.service.invoice;

import com.carpenter.core.control.service.login.PrincipalBean;
import com.carpenter.core.entity.Company_;
import com.carpenter.core.entity.dictionaries.Role;
import com.carpenter.core.entity.employee.Employee;
import com.carpenter.core.entity.employee.Employee_;
import com.carpenter.core.entity.invoice.Invoice;
import com.carpenter.core.entity.invoice.Invoice_;

import javax.ejb.Stateless;
import javax.persistence.EntityGraph;
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
import java.util.NoSuchElementException;

import static com.carpenter.utils.ConstantsRegex.FETCH_GRAPH;

@Stateless
public class InvoiceRepository implements Serializable {

    private static final long serialVersionUID = 4948654737582901416L;

    @PersistenceContext
    private EntityManager entityManager;

    public List<Invoice> findAllInvoiceByRole(PrincipalBean principalBean) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Invoice> query = builder.createQuery(Invoice.class);
        Root<Invoice> root = query.from(Invoice.class);
        root.fetch(Invoice_.employee).fetch(Employee_.addresses);
        root.fetch(Invoice_.client);

        Predicate defaultPredicate = defaultPredicate(builder, root, principalBean);
        if (defaultPredicate != null) {
            query.where(defaultPredicate);
        }
        try {
            return entityManager.createQuery(query)
                    .getResultList();
        } catch (NoResultException e) {
            return Collections.emptyList();
        }
    }

    private Predicate defaultPredicate(CriteriaBuilder builder, Root<Invoice> root, PrincipalBean principalBean) {
        Predicate predicate;
        if (principalBean.getLoggedUser().isInRole(Role.ADMINISTRATOR.name())) {
            predicate = null;
        } else if (principalBean.getLoggedUser().isInRole(Role.MANAGER.name())) {
            predicate = builder.equal(root.get(Invoice_.employee).get(Employee_.company).get(Company_.id), principalBean.getLoggedUser().getCompany().getId());
        } else {
            predicate = builder.and(
                    builder.equal(root.get(Invoice_.employee).get(Employee_.company).get(Company_.id), principalBean.getLoggedUser().getCompany().getId()),
                    builder.equal(root.get(Invoice_.employee).get(Employee_.id), principalBean.getLoggedUser().getId())
            );
        }
        return predicate;
    }

    public String findLastEmployeeInvoiceNumber(Long employeeId) {
        try {
            return entityManager.createNamedQuery("Invoice.findLastEmployeeInvoice", String.class)
                    .setParameter("employeeId", employeeId)
                    .getResultList().iterator().next();
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    public void persist(Invoice invoice) {
        entityManager.persist(invoice);
    }
}

