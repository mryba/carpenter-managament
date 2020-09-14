package com.carpenter.core.control.repository;

import com.carpenter.core.entity.employee.Address;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;

@Stateless
public class AddressRepository implements Serializable {

    @PersistenceContext
    private EntityManager entityManager;

    public void removerAddress(Address address) {
        entityManager.remove(entityManager.contains(address) ? address : entityManager.merge(address));
    }
}
