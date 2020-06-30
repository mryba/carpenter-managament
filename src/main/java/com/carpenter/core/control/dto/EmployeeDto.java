package com.carpenter.core.control.dto;

import lombok.*;

import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeDto {

    //Employee
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

    private Long presentClient;

    //Company
    private Long companyId;
    private String companyName;

    //Address
    private String city;
    private String postalCode;
    private String street;
    private String streetNumber;
    private String houseNumber;
    private String country;

    private Date birthDate;

}
