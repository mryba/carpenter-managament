package com.carpenter.core.control.repository;

import com.carpenter.core.entity.Offer;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;

@Stateless
public class OfferRepository implements Serializable {

    @PersistenceContext
    private transient EntityManager entityManager;

    public void save(Offer offer) {
        entityManager.merge(offer);
    }
}
