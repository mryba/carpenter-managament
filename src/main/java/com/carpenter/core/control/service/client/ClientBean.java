package com.carpenter.core.control.service.client;

import com.carpenter.core.control.dto.ClientDto;
import com.carpenter.core.entity.client.Client;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named("clientBean")
@ViewScoped
public class ClientBean implements Serializable {

    public static final long serialVersionUID = -98413981350873278L;

    @Inject
    ClientService clientService;

    @Getter
    @Setter
    private ClientDto clientDto;

    @PostConstruct
    public void init() {
        clientDto = new ClientDto();
    }

    public List<Client> getClientList() {
        return clientService.getClientsList();
    }

    public Client getClientByNip(String nip) {
        return clientService.getClientByNip(nip);
    }
}
