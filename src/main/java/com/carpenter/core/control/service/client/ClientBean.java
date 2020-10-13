package com.carpenter.core.control.service.client;

import com.carpenter.core.control.dto.ClientDto;
import com.carpenter.core.control.service.login.PrincipalBean;
import com.carpenter.core.entity.client.Client;
import com.carpenter.core.entity.dictionaries.Countries;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Named("clientBean")
@ViewScoped
public class ClientBean implements Serializable {

    public static final long serialVersionUID = -98413981350873278L;

    @Inject
    ClientService clientService;

    @Inject
    PrincipalBean principalBean;

    private List<ClientDto> clients;

    private ClientDto clientDto;
    private ClientMapper clientMapper;
    private String deletedClientName;


    @PostConstruct
    public void init() {
        clientDto = new ClientDto();
    }

    public List<ClientDto> getClientList() {
        clientMapper = new ClientMapper();
         clients = clientService.getClientsList(principalBean)
                .stream()
                .map(client -> clientMapper.mapToDomain(client))
                .collect(Collectors.toList());
         return clients;
    }

    public void setClient(Long clientId) {
        ClientDto client = clients.stream().filter(c -> c.getId().equals(clientId)).findFirst().orElse(null);
        if (client != null) {
            this.clientDto = client;
        }
    }

    public boolean isStreetAvailable() {
        return  clientDto != null && clientDto.getStreet() != null && !clientDto.getStreet().isEmpty();
    }

    public void saveClient() {
        Client client = clientService.createClient(clientDto);
        client.setCreateBy(principalBean.getLoggedUser().getEmail());
        client.setCreateDate(new Date());
        client.setCompany(principalBean.getLoggedUser().getCompany());

        clientService.save(client);
        clearClientForm();
    }

    public void saveEditedClient(){
        Client client = clientService.createClient(clientDto);
        if (client.getCompany() == null) {
            client.setCompany(principalBean.getLoggedUser().getCompany());
        }
        clientService.save(client);
    }


    public void deleteClient() {
        Client client = clientService.createClient(clientDto);
        client.setDeleteDate(new Date());
        client.setDeletedBy(principalBean.getLoggedUser().getEmail());
        clientService.save(client);
    }

    public void clearClientForm() {
        clientDto.setName(null);
        clientDto.setBankAccountNumber(null);
        clientDto.setNip(null);
        clientDto.setEmail(null);
        clientDto.setPhoneNumber(null);
        clientDto.setWebSite(null);
        clearClientAddress();
    }

    public void clearClientAddress(){
        clientDto.setCity(null);
        clientDto.setPostalCode(null);
        clientDto.setStreet(null);
        clientDto.setCountry(null);
        clientDto.setStreetNumber(null);
        clientDto.setHouseNumber(null);
    }

    public Countries[] getCountries() {
        return Countries.values();
    }


    public void validateDeletedClientName(FacesContext facesContext, UIComponent component, Object value) {
        if (value == null || value.toString().isEmpty()) {
            throw new ValidatorException(
                    new FacesMessage(
                            "Pole nie może być pustę"
                    )
            );
        }
        if (!value.toString().equals(clientDto.getName())) {
            throw new ValidatorException(
                    new FacesMessage(
                            "Wpisana nazwa klienta się nie zgadza",
                            "Wpisana nazwa klienta się nie zgadza"
                    )
            );
        }
    }

}

