package com.carpenter.core.control.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmployerDto {

    //Employer
    private Long id;
    private String firstName;
    private String lastName;
    @Size(max = 12, min = 12, message = "Hasło powinno zawierać 9 cyfr")
    private String phone;
    private String email;
    private String password;
    private String rePassword;
    private String nipNumber;
    private String contract;
    private String bankAccountNumber;
    private String gender;
    private boolean activeAccount;

    //Company
    private Long companyId;

    //Address
    private String city;
    private String postalCode;
    private String street;
    private String streetNumber;
    private String houseNumber;
    private String country;

}
