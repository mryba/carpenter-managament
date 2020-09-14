package com.carpenter.core.control.service.employee;

import com.carpenter.core.control.repository.AddressRepository;
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
import java.util.NoSuchElementException;

@Slf4j
@ViewScoped
@Named("employeeBean")
public class EmployeeBean implements Serializable {

    private static final long serialVersionUID = 5412921022733891332L;

    private Long employeeId;
    private boolean isAddAddress;
    private Employee editedEmployee;
    private Address editedAddress = new Address();

    @Inject
    EmployeeService employeeService;

    @Inject
    AddressRepository addressRepository;


    @PostConstruct
    public void init(){
        editedAddress = new Address();
    }

    public Employee getEditedEmployee() {
        return editedEmployee;
    }

    public Address getEditedEmployeeAddress() {
        try {
            return editedEmployee.getAddresses().iterator().next();
        } catch (NoSuchElementException e) {
            log.error("Brak adresu");
            setAddAddress(false);
        }
        return null;
    }

    public Address getEditedAddress() {
        return editedAddress;
    }

    public void setEditedAddress(Address editedAddress) {
        this.editedAddress = editedAddress;
    }

    public void setEmployee(Long employeeId) {
        this.employeeId = employeeId;
        editedEmployee = employeeService.getEmployeeById(employeeId);
        isAddAddress = editedEmployee != null && editedEmployee.getAddresses() != null && editedEmployee.getAddresses().iterator().hasNext();
    }

    public void clear() {
        employeeId = null;
        editedEmployee = null;
        editedAddress = null;
    }

    public void saveEditedEmployee() {
        if (editedEmployee.getContract() != Contract.SELF_EMPLOYMENT) {
            editedEmployee.setNipNumber(null);
        }
        if (!isAddAddress) {
            addressRepository.removerAddress(getEditedEmployeeAddress());
            editedEmployee.setAddresses(null);
        }
        if (editedAddress != null) {
            editedEmployee.addAddress(editedAddress);
        }
        employeeService.saveEmployee(editedEmployee);
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

}
