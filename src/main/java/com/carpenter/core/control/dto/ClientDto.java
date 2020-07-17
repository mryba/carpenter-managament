package com.carpenter.core.control.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClientDto {

    private Long id;
    private String name;
    private String city;
    private String postalCode;
    private String street;
    private String country;
    private String bankAccountNumber;
    private String nip;
    private String email;
    private String phoneNumber;
    private String webSite;
    private String streetNumber;
    private String houseNumber;
    private Long offerId;
}
