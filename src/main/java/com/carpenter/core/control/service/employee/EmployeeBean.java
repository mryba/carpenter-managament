package com.carpenter.core.control.service.employee;

import com.carpenter.core.control.dto.EmployeeDto;
import com.carpenter.core.entity.dictionaries.Countries;
import com.carpenter.core.entity.employee.Employee;
import org.omnifaces.cdi.Param;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

@ViewScoped
@Named("employeeBean")
public class EmployeeBean implements Serializable {

    private static final long serialVersionUID = 5412921022733891332L;

    private Long employeeId;
    private boolean isAddAddress;
    private EmployeeDto employeeDto;
    private EmployeeMapper employeeMapper;

    @Inject
    EmployeeService employeeService;

    @PostConstruct
    public void init() {
        employeeMapper = new EmployeeMapper();

    }

    public EmployeeDto getEmployeeDto() {
        return employeeDto;
    }

    public void setEmployee(Long employeeId) {
        this.employeeId = employeeId;
        Employee employee = employeeService.getEmployeeById(employeeId);
        employeeDto = employeeMapper.mapToDomain(employee);
        isAddAddress = employeeDto != null && employeeDto.getCountry() != null && employeeDto.getCity() != null;
    }

    public void clear() {
        employeeId = null;
        employeeDto = null;
    }

    public void saveEditedEmployee() {

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
