package com.carpenter.core.control.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClientDto {

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

}
