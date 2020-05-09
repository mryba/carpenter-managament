package com.carpenter.core.control.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmployerDto {

    //Employer
    private CompanyDto companyDto;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private String password;
    private String rePassword;
    private String nipNumber;
    private String contract;
    private String bankAccountNumber;
    private String gender;

    //Address
    private String city;
    private String postalCode;
    private String street;
    private String streetNumber;
    private String houseNumber;
    private String country;
}
