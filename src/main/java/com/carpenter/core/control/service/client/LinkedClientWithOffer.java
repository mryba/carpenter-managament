package com.carpenter.core.control.service.client;

import com.carpenter.core.control.dto.ClientDto;
import lombok.extern.slf4j.Slf4j;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Slf4j
@ViewScoped
@Named("linkedClientWithOffer")
public class LinkedClientWithOffer implements Serializable {

    @Inject
    ClientService clientService;

    private Long offerId;
    private Long clientId;

    public Long getOfferId() {
        return offerId;
    }

    public void setOfferId(Long offerId) {
        this.offerId = offerId;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public List<ClientDto> getAllClients(){
       return clientService.getAllAvailableClients();
    }
}
