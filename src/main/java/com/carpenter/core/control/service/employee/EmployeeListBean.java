package com.carpenter.core.control.service.employee;

import com.carpenter.core.control.dto.ClientDto;
import com.carpenter.core.control.dto.CompanyDto;
import com.carpenter.core.control.dto.EmployeeDto;
import com.carpenter.core.control.service.client.ClientService;
import com.carpenter.core.control.service.company.CompanyService;
import com.carpenter.core.control.service.employeegroup.EmployeeGroupBean;
import com.carpenter.core.control.service.employeegroup.EmployeeGroupService;
import com.carpenter.core.control.service.login.PrincipalBean;
import com.carpenter.core.control.utils.logger.Logged;
import com.carpenter.core.entity.Company;
import com.carpenter.core.entity.client.Client;
import com.carpenter.core.entity.dictionaries.Contract;
import com.carpenter.core.entity.dictionaries.Countries;
import com.carpenter.core.entity.dictionaries.Gender;
import com.carpenter.core.entity.dictionaries.Role;
import com.carpenter.core.entity.employee.Employee;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.PostConstruct;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Logged
@Named("employeeListBean")
@ViewScoped
public class EmployeeListBean implements Serializable {

    private static final long serialVersionUID = 7312921022733891332L;

    @Inject
    EmployeeService employeeService;

    @Inject
    CompanyService companyService;

    @Inject
    PrincipalBean principalBean;

    @Inject
    EmployeeValidation employeeValidation;

    @Inject
    ClientService clientService;

    @Inject
    EmployeeGroupBean employeeGroupBean;

    @Getter
    @Setter
    private EmployeeDto employeeDto;

    private List<ClientDto> clients = new LinkedList<>();

    private boolean isAddAddress;
    private boolean isAccountCreate;
    private boolean hasLoggedUserAdminOrManagerRole;

    @PostConstruct
    public void init() {
        employeeDto = new EmployeeDto();
    }

    public void saveEmployee() {
        Employee employee = employeeService.createEmployee(employeeDto, principalBean);

        Company company = getCompanyBasedOnLoggedUser();
        employee.setCompany(company);

        if (isAccountCreate) {
            String password = employeeValidation.getHashedPassword();
            employee.setPassword(password);
        }
        employeeService.saveEmployee(employee);
    }

    public Company getCompanyBasedOnLoggedUser() {
        if (principalBean.getLoggedUser().isInRole(Role.ADMINISTRATOR.name()) && employeeDto.getCompanyId() != null) {
            return companyService.getCompanyById(employeeDto.getCompanyId());
        }

        if (principalBean.getLoggedUser().isInRole(Role.MANAGER.name())) {
            return principalBean.getLoggedUser().getCompany();
        }
        return null;
    }

    public List<Employee> getEmployersList() {
        return employeeService.getEmployersList();
    }

    public List<Contract> getAvailableContracts() {
        return Stream.of(Contract.values()).collect(Collectors.toList());
    }

    public boolean isSelfEmploymentOfContract() {
        if (employeeDto != null) {
            String contract = employeeDto.getContract();
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
        employeeDto.setFirstName(null);
        employeeDto.setLastName(null);
        employeeDto.setEmail(null);
        employeeDto.setNipNumber(null);
        employeeDto.setPhone(null);

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
        return isAddAddress && employeeDto.getStreet() != null && !employeeDto.getStreet().isEmpty();
    }

    public Countries[] getCountries() {
        return Countries.values();
    }

    private void resetEmployerAddressValues() {
        employeeDto.setCity(null);
        employeeDto.setPostalCode(null);
        employeeDto.setStreet(null);
        employeeDto.setStreetNumber(null);
        employeeDto.setHouseNumber(null);
        employeeDto.setCountry(null);
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
            employeeDto.setPassword(null);
            employeeDto.setRePassword(null);
        }
    }

    public boolean getHasLoggedUserAdminOrManagerRole() {
        this.hasLoggedUserAdminOrManagerRole = principalBean.getLoggedUser().isInRole(Arrays.asList(Role.MANAGER, Role.ADMINISTRATOR));
        return hasLoggedUserAdminOrManagerRole;
    }

    public List<ClientDto> getAvailableClients() {
        clients.clear();
        clients.addAll(clientService.getAllAvailableClients());
        return clients;
    }


    public void unableAddress() {
        if (employeeDto.getContract().equals("SELF_EMPLOYMENT")) {
            setAddAddress(true);
        } else {
            setAddAddress(false);
        }
    }

    public void resetContract(){
        if (!isAddAddress) {
            employeeDto.setContract("WITHOUT_A_CONTRACT");
        }
    }
}
