package com.carpenter.core.control.repository;

import com.carpenter.core.entity.client.Client;
import lombok.extern.slf4j.Slf4j;

import javax.ejb.Stateless;
import javax.persistence.*;
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

    public List<Client> getAllClients() {
        try {
            return entityManager.createNamedQuery("Client.findAll", Client.class)
                    .getResultList();
        } catch (NoResultException e) {
            log.error("No clients found!");
            return Collections.emptyList();
        }
    }

    public void addClient(Client client) {
        entityManager.merge(client);
    }

    public Client getClientById(Long id) {
        try {
            return  entityManager.createQuery("SELECT c FROM Client c LEFT JOIN FETCH c.invoices WHERE c.id =:id", Client.class)
                    .setParameter("id", id)
                    .getResultList().iterator().next();
        } catch (NoResultException e) {
            return null;
        }
    }
}