package com.carpenter.core.control.service.invoice;

import com.carpenter.core.entity.invoice.Invoice;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;

@Stateless
public class InvoiceRepository implements Serializable {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Invoice> findAllInvoices() {
        try {
           return entityManager.createNamedQuery("Invoice.findAllInvoices", Invoice.class)
                    .getResultList();
        } catch (NoResultException e) {
            return Collections.emptyList();
        }
    }
}

