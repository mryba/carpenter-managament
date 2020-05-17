package com.carpenter.core.control.service.client;

import com.carpenter.core.control.dto.ClientDto;
import com.carpenter.core.control.repository.ClientRepository;
import com.carpenter.core.entity.client.Client;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.List;

@SessionScoped
public class ClientService implements Serializable {

    public static final long serialVersionUID = 3281746910120923L;

    @Inject
    ClientRepository clientRepository;

    private ClientMapper clientMapper;

    public Client getClientByNip(String nip) {
        return clientRepository.getClientByNIP(nip);
    }

    public List<Client> getClientsList(){
        return clientRepository.getAllClients();
    }

    public Client createClient(ClientDto clientDto) {
        clientMapper = new ClientMapper();
        return clientMapper.mapFromDomain(clientDto);
    }

    public void addClient(Client client) {
        clientRepository.addClient(client);
    }
}
