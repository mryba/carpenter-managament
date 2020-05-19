package com.carpenter.core.control.service.employee;

import com.carpenter.core.control.dto.CompanyDto;
import com.carpenter.core.control.dto.EmployerDto;
import com.carpenter.core.control.service.company.CompanyService;
import com.carpenter.core.control.service.login.PrincipalBean;
import com.carpenter.core.control.utils.logger.Logged;
import com.carpenter.core.entity.Company;
import com.carpenter.core.entity.dictionaries.Contract;
import com.carpenter.core.entity.dictionaries.Countries;
import com.carpenter.core.entity.dictionaries.Gender;
import com.carpenter.core.entity.dictionaries.Role;
import com.carpenter.core.entity.employee.Employer;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.PostConstruct;
import javax.faces.component.UIInput;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.xml.registry.SaveException;
import java.io.Serializable;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Logged
@Named("employerBean")
@ViewScoped
public class EmployerBean implements Serializable {

    private static final long serialVersionUID = 7312921022733891332L;

    @Inject
    EmployerService employerService;

    @Inject
    CompanyService companyService;

    @Inject
    PrincipalBean principalBean;

    @Inject
    EmployeeValidation employeeValidation;

    @Getter
    @Setter
    private EmployerDto employerDto;

    private boolean isAddAddress;
    private boolean isAccountCreate;

    @PostConstruct
    public void init() {
        employerDto = new EmployerDto();
    }

    public void saveEmployee() {
        Employer employee = employerService.createEmployee(employerDto);

        Company company = getCompanyBasedOnLoggedUser();
        employee.setCompany(company);

        if (isAccountCreate) {
            String password = employeeValidation.getHashedPassword();
            employee.setPassword(password);
        }
        employerService.saveEmployee(employee);
    }

    public Company getCompanyBasedOnLoggedUser() {
        if (principalBean.getLoggedUser().isInRole(Role.ADMINISTRATOR.name()) && employerDto.getCompanyId() != null) {
            return companyService.getCompanyById(employerDto.getCompanyId());
        }

        if (principalBean.getLoggedUser().isInRole(Role.MANAGER.name())) {
            return principalBean.getLoggedUser().getCompany();
        }
        return null;
    }

    public List<Employer> getEmployersList() {
        return employerService.getEmployersList();
    }

    public List<Contract> getAvailableContracts() {
        return Stream.of(Contract.values()).collect(Collectors.toList());
    }

    public boolean isSelfEmploymentOfContract() {
        if (employerDto != null) {
            String contract = employerDto.getContract();
            if (contract != null) {
                return contract.equals("SELF_EMPLOYMENT");
            }
        }
        return false;
    }

    public Gender[] getGenders() {
        return Gender.values();
    }

    public void cleanEmployeeForm() {
        employerDto.setFirstName(null);
        employerDto.setLastName(null);
        employerDto.setEmail(null);
        employerDto.setNipNumber(null);
        employerDto.setPhone(null);

        setAccountCreate(false);
        setAddAddress(false);
    }

    public boolean isAddAddress() {
        return isAddAddress;
    }

    public void setAddAddress(boolean isAddAddress) {
        this.isAddAddress = isAddAddress;
        if (!isAddAddress) {
            resetEmployerAddressValues();
        }
    }

    public boolean isStreetNumberActive() {
        return isAddAddress && employerDto.getStreet() != null && !employerDto.getStreet().isEmpty();
    }

    public Countries[] getCountries() {
        return Countries.values();
    }

    private void resetEmployerAddressValues() {
        employerDto.setCity(null);
        employerDto.setPostalCode(null);
        employerDto.setStreet(null);
        employerDto.setStreetNumber(null);
        employerDto.setHouseNumber(null);
        employerDto.setCountry(null);
    }

    public List<CompanyDto> getCompanies() {
        return companyService.getAllActiveCompanies();
    }

    public boolean isAccountCreate() {
        return isAccountCreate;
    }

    public void setAccountCreate(boolean isAccountCreate) {
        this.isAccountCreate = isAccountCreate;
        if (!isAccountCreate) {
            employerDto.setPassword(null);
            employerDto.setRePassword(null);
        }
    }
}
