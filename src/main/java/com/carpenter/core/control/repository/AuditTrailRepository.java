package com.carpenter.core.control.repository;

import com.carpenter.core.entity.AuditTrail;
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
public class AuditTrailRepository implements Serializable {
    private static final long serialVersionUID = 4989382410328475L;

    @PersistenceContext
    private transient EntityManager entityManager;

    public List<AuditTrail> getAllAudiTrails() {
        try {
            return entityManager.createNamedQuery("AuditTrail.findAll", AuditTrail.class).getResultList();
        } catch (NoResultException e) {
            log.error("No Audit Trails");
            return Collections.emptyList();
        }
    }

    public void addAuditTrail(AuditTrail auditTrail) {
        entityManager.merge(auditTrail);
    }
}
