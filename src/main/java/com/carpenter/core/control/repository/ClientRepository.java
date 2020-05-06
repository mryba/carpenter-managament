package com.carpenter.core.control.repository;

import com.carpenter.core.entity.client.Client;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.List;

@Stateless
public class ClientRepository implements Serializable {

    private static final long serialVersionUID = 186579202498762649L;

    @PersistenceContext
    private transient EntityManager entityManager;

    public Client getClientByNIP(String nip) {
        return entityManager.createNamedQuery("Client.findByNIP", Client.class)
                .setParameter("nip", nip).getResultList().stream().findFirst().orElse(null);
    }

    public List<Client> getAllClients() {
        return entityManager.createNamedQuery("Client.findAll", Client.class).getResultList();
    }

}
