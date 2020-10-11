package com.carpenter.core.control.repository;

import com.carpenter.core.control.service.login.PrincipalBean;
import com.carpenter.core.entity.Company_;
import com.carpenter.core.entity.client.Client;
import com.carpenter.core.entity.client.Client_;
import com.carpenter.core.entity.dictionaries.Role;
import com.carpenter.core.entity.employee.Employee_;
import com.carpenter.core.entity.invoice.Invoice;
import com.carpenter.core.entity.invoice.Invoice_;
import lombok.extern.slf4j.Slf4j;

import javax.ejb.Stateless;
import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import static com.carpenter.utils.ConstantsRegex.FETCH_GRAPH;

@Slf4j
@Stateless
public class ClientRepository implements Serializable {

    private static final long serialVersionUID = 186579202498762649L;

    @PersistenceContext
    private transient EntityManager entityManager;

    public Client getClientByNIP(String nip) {
        return entityManager.createNamedQuery("Client.findByNip", Client.class)
                .setParameter("nip", nip).getResultList().stream().findFirst().orElse(null);
    }

    public List<Client> getAllClientByCompany(PrincipalBean principalBean) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Client> query = builder.createQuery(Client.class);
        Root<Client> root = query.from(Client.class);

        Predicate defaultPredicate = defaultPredicate(builder, root, principalBean);
        if (defaultPredicate != null) {
            query.where(defaultPredicate);
        }
        try {
            return entityManager.createQuery(query).getResultList();
        } catch (NoResultException e) {
            return Collections.emptyList();
        }
    }

    private Predicate defaultPredicate(CriteriaBuilder builder, Root<Client> root, PrincipalBean principalBean) {
        Predicate predicate = null;
        if (principalBean.getLoggedUser().isInRole(Role.ADMINISTRATOR.name())) {
            predicate = null;
        } else if (principalBean.getLoggedUser().isInRole(Role.MANAGER.name()) || principalBean.getLoggedUser().isInRole(Role.EMPLOYER.name())) {
            predicate = builder.equal(root.get(Client_.company).get(Company_.id), principalBean.getLoggedUser().getCompany().getId());
        }
        return predicate;
    }

    public void addClient(Client client) {
        entityManager.merge(client);
    }

    public Client getClientById(Long id) {
        try {
            return entityManager.createQuery("SELECT c FROM Client c LEFT JOIN FETCH c.invoices WHERE c.id =:id", Client.class)
                    .setParameter("id", id)
                    .getResultList().iterator().next();
        } catch (NoResultException e) {
            return null;
        }
    }
}