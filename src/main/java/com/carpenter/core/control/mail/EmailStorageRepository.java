package com.carpenter.core.control.mail;

import com.carpenter.core.entity.email.EmailStatus;
import com.carpenter.core.entity.email.EmailStorage;
import com.carpenter.core.entity.email.EmailStorage_;

import javax.ejb.Stateless;
import javax.persistence.Entity;
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

@Stateless
public class EmailStorageRepository implements Serializable {

    private static final long serialVersionUID = -1507608432729903505L;

    @PersistenceContext
    private EntityManager entityManager;

    public void persistEmail(EmailStorage emailStorage) {
        entityManager.persist(emailStorage);
    }

    public List<EmailStorage> findAllNoSendedEmails() {
        try {
            return entityManager.createQuery("SELECT em FROm EmailStorage em WHERE em.status ='NOT_SENT' AND em.deletedBy IS NULL AND em.deleteDate IS NULL", EmailStorage.class)
                    .getResultList();
        } catch (NoResultException e) {
            return Collections.emptyList();
        }
    }

    public void updateEmail(EmailStorage emailStorage) {
        entityManager.merge(emailStorage);
    }
}
