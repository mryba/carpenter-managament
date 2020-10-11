package com.carpenter.core.control.service.client;

import com.carpenter.core.control.dto.ClientDto;
import com.carpenter.core.control.repository.ClientRepository;
import com.carpenter.core.control.service.login.PrincipalBean;
import com.carpenter.core.entity.client.Client;

import javax.ejb.Stateless;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

@Stateless
public class ClientService implements Serializable {

    public static final long serialVersionUID = 4381746910120923L;

    @Inject
    ClientRepository clientRepository;

    private ClientMapper clientMapper;

    public Client getClientByNip(String nip) {
        return clientRepository.getClientByNIP(nip);
    }

    public Client getClientById(Long id) {
        return clientRepository.getClientById(id);
    }

    public List<Client> getClientsList(PrincipalBean principalBean) {
        return clientRepository.getAllClientByCompany(principalBean);
    }

    public List<ClientDto> getAllAvailableClients(PrincipalBean principalBean) {
        List<ClientDto> toReturn = new LinkedList<>();
        clientMapper = new ClientMapper();
        clientRepository.getAllClientByCompany(principalBean).forEach(e -> toReturn.add(clientMapper.mapToDomain(e)));
        return toReturn;
    }

    public Client createClient(ClientDto clientDto) {
        clientMapper = new ClientMapper();
        return clientMapper.mapFromDomain(clientDto);
    }

    public void save(Client client) {
        clientRepository.addClient(client);
    }

    public boolean isClientNew(String nip) {
        return getClientByNip(nip) == null;
    }

    public Client mapClientToDomain(ClientDto clientDto){
        return clientMapper.mapFromDomain(clientDto);
    }
}
