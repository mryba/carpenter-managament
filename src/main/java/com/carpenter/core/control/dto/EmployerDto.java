package com.carpenter.core.control.dto;

import com.carpenter.core.entity.dictionaries.Contract;
import com.carpenter.core.entity.dictionaries.Gender;
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
    private String companyName;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private String nipNumber;
    private String contract;
    private String gender;

    //Address
    private String city;
    private String postalCode;
    private String street;
    private String streetNumber;
    private String houseNumber;
    private String country;
}
