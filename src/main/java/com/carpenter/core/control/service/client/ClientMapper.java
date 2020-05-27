package com.carpenter.core.control.service.client;

import com.carpenter.core.control.dto.ClientDto;
import com.carpenter.core.entity.client.Client;
import com.carpenter.utils.Mapper;

public class ClientMapper implements Mapper<Client, ClientDto> {

    @Override
    public Client mapFromDomain(ClientDto clientDto) {
        return Client.builder()
                .name(clientDto.getName())
                .nip(clientDto.getNip())
                .email(clientDto.getEmail())
                .phoneNumber(clientDto.getPhoneNumber())
                .webSite(clientDto.getWebSite())
                .bankAccountNumber(clientDto.getBankAccountNumber())
                .country(clientDto.getCountry())
                .city(clientDto.getCity())
                .postalCode(clientDto.getPostalCode())
                .street(clientDto.getStreet())
                .streetNumber(clientDto.getStreetNumber())
                .houseNumber(clientDto.getHouseNumber())
                .build();
    }

    @Override
    public ClientDto mapToDomain(Client client) {
        return null;
    }
}
