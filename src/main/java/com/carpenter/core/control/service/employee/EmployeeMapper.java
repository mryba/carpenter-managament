package com.carpenter.core.control.service.employee;

import com.carpenter.core.control.dto.EmployeeDto;
import com.carpenter.core.control.service.client.ClientMapper;
import com.carpenter.core.entity.dictionaries.Contract;
import com.carpenter.core.entity.dictionaries.Gender;
import com.carpenter.core.entity.dictionaries.Role;
import com.carpenter.core.entity.employee.Address;
import com.carpenter.core.entity.employee.Employee;
import com.carpenter.utils.Mapper;

public class EmployeeMapper implements Mapper<Employee, EmployeeDto> {


    @Override
    public Employee mapFromDomain(EmployeeDto employeeDto) {
        Employee employee = Employee.builder()
                .email(employeeDto.getEmail())
                .password(employeeDto.getPassword())
                .nipNumber(employeeDto.getNipNumber())
                .contract(Contract.valueOf(employeeDto.getContract()))
                .bankAccountNumber(employeeDto.getBankAccountNumber())
                .firstName(employeeDto.getFirstName())
                .lastName(employeeDto.getLastName())
                .gender(Gender.valueOf(employeeDto.getGender()))
                .phoneNumber(employeeDto.getPhone())
                .accountActive(Boolean.TRUE)
                .birthDate(employeeDto.getBirthDate())
                .build();

        if (employeeDto.getId() != null) {
            employee.setId(employeeDto.getId());
        }
        employee.addRole(Role.EMPLOYER);
        employee.addAddress(mapAddressToDomain(employeeDto));
        return employee;
    }

    private Address mapAddressToDomain(EmployeeDto employeeDto) {
        if (employeeDto.getCity() != null && employeeDto.getPostalCode() != null) {
            return new Address(
                    employeeDto.getCity(), employeeDto.getPostalCode(), employeeDto.getStreet(), employeeDto.getStreetNumber(), employeeDto.getHouseNumber(), employeeDto.getCountry());
        }
        return new Address();
    }

    @Override
    public EmployeeDto mapToDomain(Employee employee) {
        EmployeeDto employeeDto = EmployeeDto.builder()
                .id(employee.getId())
                .email(employee.getEmail())
                .nipNumber(employee.getNipNumber() != null ? employee.getNipNumber() : "")
                .contract(employee.getContract().name())
                .bankAccountNumber(employee.getBankAccountNumber() != null ? employee.getBankAccountNumber() : "")
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .gender(employee.getGender().name())
                .phone(employee.getPhoneNumber() != null ? employee.getPhoneNumber() : "")
                .activeAccount(employee.getAccountActive())
                .build();

        if (employee.getAddresses() != null && !employee.getAddresses().isEmpty()) {
            employeeDto.setCity(employee.getAddresses().iterator().next().getCity());
            employeeDto.setPostalCode(employee.getAddresses().iterator().next().getPostalCode());
            employeeDto.setStreet(employee.getAddresses().iterator().next().getStreetNumber());
            employeeDto.setHouseNumber(employee.getAddresses().iterator().next().getHouseNumber());
            employeeDto.setCountry(employee.getAddresses().iterator().next().getCountry());
        }

        if (employee.getCompany() != null) {
            employeeDto.setCompanyId(employee.getCompany().getId());
            employeeDto.setCompanyName(employee.getCompany().getName());
        }
        return employeeDto;
    }
}
