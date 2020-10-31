package com.carpenter.core.control.repository;

import com.carpenter.core.control.dto.OfferDto;
import com.carpenter.core.control.service.login.PrincipalBean;
import com.carpenter.core.entity.*;
import com.carpenter.core.entity.dictionaries.Role;
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

@Slf4j
@Stateless
public class OfferRepository implements Serializable {

    @PersistenceContext
    private transient EntityManager entityManager;

    public void save(Offer offer) {
        entityManager.merge(offer);
    }

    public void changeToRead(Long id) {
        Offer offer = entityManager.find(Offer.class, id);
        if (offer == null || offer.getIsRead()) {
            return;
        }
        offer.setIsRead(Boolean.TRUE);
        entityManager.merge(offer);
    }

    public void remove(Long id) {
        Offer offer = entityManager.find(Offer.class, id);
        entityManager.remove(offer);
        log.info("REMOVED: " + id);
    }

    public List<Offer> getOffersByCompany(Long companyId) {
        try {
            return entityManager.createQuery("SELECT o from Offer o LEFT JOIN FETCH o.company WHERE o.company.id =: companyId ORDER BY o.createDate DESC", Offer.class)
                    .setParameter("companyId", companyId)
                    .getResultList();
        } catch (NoResultException e) {
            log.error("No offers found!");
            return Collections.emptyList();
        }
    }

    public Offer findOfferById(Long id) {
        try {
            return entityManager.createNamedQuery("Offer.findOfferById", Offer.class)
                    .setParameter("offerId", id).getResultList().iterator().next();
        } catch (NonUniqueResultException e) {
            return null;
        }
    }

    public List<Offer> getAllOffers() {
        try {
            return entityManager.createQuery("SELECT o FROM Offer o WHERE o.deleteDate IS NULL ORDER BY o.createDate DESC ", Offer.class)
                    .getResultList();
        } catch (NoResultException e) {
            return Collections.emptyList();
        }
    }

    public Collection<Offer> findAllOffersByFilter(PrincipalBean principalBean, int pageSize, int currentPage) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Offer> query = builder.createQuery(Offer.class);
        Root<Offer> root = query.from(Offer.class);

        Predicate defaultPredicate = getDefaultPredicate(builder, root, principalBean);

        if (defaultPredicate != null) {
            query.where(defaultPredicate);
        }

        try {
            return entityManager.createQuery(query)
                    .setFirstResult((currentPage - 1) * pageSize)
                    .setMaxResults(pageSize)
                    .getResultList();
        } catch (NoResultException e) {
            return Collections.emptyList();
        }

    }


    public Long countAllOffersByFilter(PrincipalBean principalBean) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = builder.createQuery(Long.class);

        Root<Offer> root = query.from(Offer.class);
        query.select(builder.count(root));

        Predicate filterPredicate = getDefaultPredicate(builder, root, principalBean);
        if (filterPredicate != null) {
            query.where(filterPredicate);
        }

        try {
            return entityManager.createQuery(query).getResultList().stream().findFirst().orElse(0L);
        } catch (NoResultException e) {
            return 0L;
        }
    }

    private Predicate getDefaultPredicate(CriteriaBuilder builder, Root<Offer> root, PrincipalBean principalBean) {
        Predicate predicate = null;
        if (principalBean.getLoggedUser().isInRole(Role.ADMINISTRATOR.name())) {
            //nothing just chilling
        } else {
            predicate = builder.equal(root.get(Offer_.company).get(Company_.id), principalBean.getLoggedUser().getCompany().getId());
        }
        return predicate;
    }

}
