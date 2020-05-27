package com.carpenter.core.control.repository;

import com.carpenter.core.entity.Offer;
import lombok.extern.slf4j.Slf4j;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
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

    public void remove(Long id) {
        Offer offer = entityManager.find(Offer.class, id);
        entityManager.remove(offer);
        log.info("REMOVED: " + id);
    }

    public List<Offer> getOffersByCompany(Long companyId) {
        try {
            return entityManager.createQuery("SELECT o from Offer o LEFT JOIN FETCH o.company WHERE o.company.id =: companyId", Offer.class)
                    .setParameter("companyId", companyId)
                    .getResultList();
        } catch (NoResultException e) {
            log.error("No offers found!");
            return Collections.emptyList();
        }
    }
}
