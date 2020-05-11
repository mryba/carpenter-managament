package com.carpenter.core.control.service.employee;

import com.carpenter.core.control.dto.EmployerDto;
import com.carpenter.core.entity.dictionaries.Contract;
import com.carpenter.core.entity.dictionaries.Gender;
import com.carpenter.core.entity.dictionaries.Role;
import com.carpenter.core.entity.employee.Address;
import com.carpenter.core.entity.employee.Employer;
import com.carpenter.utils.Mapper;

public class EmployeeMapper implements Mapper<Employer, EmployerDto> {

    @Override
    public Employer mapFromDomain(EmployerDto employerDto) {
        Employer employer = Employer.builder()
                .email(employerDto.getEmail())
                .password(employerDto.getPassword())
                .nipNumber(employerDto.getNipNumber())
                .contract(Contract.valueOf(employerDto.getContract()))
                .bankAccountNumber(employerDto.getBankAccountNumber())
                .firstName(employerDto.getFirstName())
                .lastName(employerDto.getLastName())
                .gender(Gender.valueOf(employerDto.getGender()))
                .phoneNumber(employerDto.getPhone())
                .accountActive(Boolean.TRUE)
                .build();

        employer.addRole(Role.EMPLOYER);
        employer.addAddress(mapAddressToDomain(employerDto));
        return employer;
    }

    private Address mapAddressToDomain(EmployerDto employerDto) {
        if (employerDto.getCity() != null && employerDto.getPostalCode() != null) {
            return new Address(
                    employerDto.getCity(), employerDto.getPostalCode(), employerDto.getStreet(), employerDto.getStreetNumber(), employerDto.getHouseNumber(), employerDto.getCountry());
        }
        return new Address();
    }

    @Override
    public EmployerDto mapToDomain(Employer employer) {
        return null;
    }
}
