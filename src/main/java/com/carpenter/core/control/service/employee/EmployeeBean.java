package com.carpenter.core.control.service.employee;

import com.carpenter.core.control.repository.AddressRepository;
import com.carpenter.core.control.service.login.PrincipalBean;
import com.carpenter.core.entity.dictionaries.Contract;
import com.carpenter.core.entity.dictionaries.Countries;
import com.carpenter.core.entity.employee.Address;
import com.carpenter.core.entity.employee.Employee;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Date;
import java.util.NoSuchElementException;

@Slf4j
@ViewScoped
@Named("employeeBean")
public class EmployeeBean implements Serializable {

    private static final long serialVersionUID = 5412921022733891332L;

    private boolean isAddAddress;
    private Employee editedEmployee;
    private Address editedAddress = new Address();

    @Inject
    EmployeeService employeeService;

    @Inject
    AddressRepository addressRepository;

    @Inject
    PrincipalBean principalBean;

    @PostConstruct
    public void init() {
        editedAddress = new Address();
    }

    public Employee getEditedEmployee() {
        return editedEmployee;
    }

    public Address getEditedEmployeeAddress() {
        try {
            return editedAddress = editedEmployee.getAddresses().iterator().next();
        } catch (NoSuchElementException e) {
            log.info("PracowniK: {}, - brak adresu", editedEmployee.getEmail());
            return editedAddress;
        }
    }

    public void setEmployee(Long employeeId) {
        editedEmployee = employeeService.getEmployeeById(employeeId);
        isAddAddress = editedEmployee != null && editedEmployee.getAddresses() != null && editedEmployee.getAddresses().iterator().hasNext();
        getEditedEmployeeAddress();
    }

    public Address getEditedAddress() {
        return editedAddress;
    }

    public void setEditedAddress(Address editedAddress) {
        this.editedAddress = editedAddress;
    }

    public void clear() {
        editedEmployee = null;
        editedAddress = new Address();
        isAddAddress = false;
    }

    public void saveEditedEmployee() {
        if (editedEmployee.getContract() != Contract.SELF_EMPLOYMENT) {
            editedEmployee.setNipNumber(null);
        }
        if (!isAddAddress) {
            addressRepository.removerAddress(getEditedEmployeeAddress());
            editedEmployee.setAddresses(null);
        } else {
            if (editedAddress.getCreateDate() ==null && editedAddress.getCreateBy() ==null) {
                editedAddress.setCreateDate(new Date());
                editedAddress.setCreateBy(principalBean.getLoggedUser().getEmail());
            }
            editedEmployee.addAddress(editedAddress);
        }
        employeeService.saveEmployee(editedEmployee);
        clear();
    }

    public Countries[] getCountries() {
        return Countries.values();
    }

    public boolean isAddAddress() {
        return isAddAddress;
    }

    public void setAddAddress(boolean employeeAddressSigned) {
        this.isAddAddress = employeeAddressSigned;
    }

    public void unableAddress() {
        if (editedEmployee.getContract() == Contract.SELF_EMPLOYMENT) {
            setAddAddress(true);
        } else {
            setAddAddress(false);
        }
    }

    public void resetContract() {
        if (!isAddAddress) {
            editedEmployee.setContract(Contract.WITHOUT_A_CONTRACT);
        }
    }

    public boolean isStreetNumberActive() {
        return isAddAddress && editedAddress.getStreet() != null && !editedAddress.getStreet().isEmpty();
    }
}