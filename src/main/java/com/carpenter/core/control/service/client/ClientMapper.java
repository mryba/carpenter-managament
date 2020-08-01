package com.carpenter.core.control.service.client;

import com.carpenter.core.control.dto.ClientDto;
import com.carpenter.core.entity.client.Client;
import com.carpenter.utils.Mapper;

public class ClientMapper implements Mapper<Client, ClientDto> {

    @Override
    public Client mapFromDomain(ClientDto clientDto) {
        Client client = Client.builder()
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

        if (clientDto.getId() != null) {
            client.setId(clientDto.getId());
        }
        return client;

    }

    @Override
    public ClientDto mapToDomain(Client client) {
        return ClientDto.builder()
                .id(client.getId())
                .name(client.getName())
                .nip(client.getNip())
                .email(client.getEmail())
                .phoneNumber(client.getPhoneNumber())
                .webSite(client.getWebSite())
                .bankAccountNumber(client.getBankAccountNumber())
                .country(client.getCountry())
                .city(client.getCity())
                .postalCode(client.getPostalCode())
                .street(client.getStreet())
                .streetNumber(client.getStreetNumber())
                .houseNumber(client.getHouseNumber())
                .build();
    }
}
