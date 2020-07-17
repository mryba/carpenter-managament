package com.carpenter.core.control.service.client;

import com.carpenter.core.control.dto.ClientDto;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Slf4j
@ViewScoped
@Getter
@Setter
@Named("linkedClientWithOffer")
public class LinkedClientWithOffer implements Serializable {

    @Inject
    ClientService clientService;

    @Inject
    ClientBean clientBean;

    private Long offerId;
    private Long clientId;
    private boolean createNewClient = false;

    public List<ClientDto> getAllClients() {
        return clientService.getAllAvailableClients();
    }

    public void setCreateNewClient(boolean createNewClient) {
        this.createNewClient = createNewClient;
        if (createNewClient) {
            clientBean.getClientDto().setOfferId(offerId);
        }
    }
}
