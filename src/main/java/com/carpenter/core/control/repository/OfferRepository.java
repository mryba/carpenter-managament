package com.carpenter.core.control.repository;

import com.carpenter.core.entity.Offer;
import lombok.extern.slf4j.Slf4j;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
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
}
