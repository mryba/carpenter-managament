package com.carpenter.core.control.service.client;

import com.carpenter.core.control.dto.ClientDto;
import com.carpenter.core.control.dto.CompanyDto;
import com.carpenter.core.control.service.company.CompanyService;
import com.carpenter.core.control.service.login.PrincipalBean;
import com.carpenter.core.control.service.offer.OfferService;
import com.carpenter.core.entity.Offer;
import com.carpenter.core.entity.client.Client;
import com.carpenter.core.entity.dictionaries.Countries;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Named("clientBean")
@ViewScoped
public class ClientBean implements Serializable {

    public static final long serialVersionUID = -98413981350873278L;

    @Inject
    ClientService clientService;

    @Inject
    CompanyService companyService;

    @Inject
    PrincipalBean principalBean;

    @Getter
    @Setter
    private ClientDto clientDto;

    private CompanyDto companyDto;

    private ClientMapper clientMapper;

    private List<CompanyDto> allActiveCompanies;

    private boolean includeAddress;
    private Long offerId;

    public boolean isIncludeAddress() {
        return includeAddress;
    }

    public void setIncludeAddress(boolean includeAddress) {
        this.includeAddress = includeAddress;
    }

    @PostConstruct
    public void init() {
        clientDto = new ClientDto();
        allActiveCompanies = companyService.getAllActiveCompanies();
    }

    public List<ClientDto> getClientList() {
        clientMapper = new ClientMapper();
        return clientService.getClientsList()
                .stream()
                .map(client -> clientMapper.mapToDomain(client))
                .collect(Collectors.toList());
    }

    public void saveClient() {
        Client client = clientService.createClient(clientDto);
        client.setCreateBy(principalBean.getLoggedUser().getEmail());
        client.setCreateDate(new Date());

        clientService.save(client);
    }

    public void clearClientForm() {
        clientDto.setName(null);
        clientDto.setCity(null);
        clientDto.setPostalCode(null);
        clientDto.setStreet(null);
        clientDto.setCountry(null);
        clientDto.setBankAccountNumber(null);
        clientDto.setNip(null);
        clientDto.setEmail(null);
        clientDto.setPhoneNumber(null);
        clientDto.setWebSite(null);
        clientDto.setStreetNumber(null);
        clientDto.setHouseNumber(null);
    }

    public Countries[] getCountries() {
        return Countries.values();
    }

    public List<CompanyDto> getAllActiveCompanies() {
        return allActiveCompanies;
    }

    public CompanyDto getCompanyDto() {
        return companyDto;
    }

    public void setCompanyDto(CompanyDto companyDto) {
        this.companyDto = companyDto;
    }

    public void savePresentClient() {
        ClientDto clientDto = this.clientDto;
    }


    public Long getOfferId() {
        return offerId;
    }

    public void setOfferId(Long offerId) {
        this.offerId = offerId;
    }
}
