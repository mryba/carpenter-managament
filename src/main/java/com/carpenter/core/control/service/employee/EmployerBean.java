package com.carpenter.core.control.service.employee;

import com.carpenter.core.control.dto.EmployerDto;
import com.carpenter.core.entity.dictionaries.Contract;
import com.carpenter.core.entity.employee.Employer;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Named("employerBean")
@ViewScoped
public class EmployerBean implements Serializable {

    private static final long serialVersionUID = 7312921022733891332L;

    @Inject
    EmployerService employerService;

    @Getter
    @Setter
    private EmployerDto employerDto;

    @PostConstruct
    public void init() {
        employerDto = new EmployerDto();
    }

    public List<Employer> getEmployersList() {
        return employerService.getEmployersList();
    }

    public Set<Contract> getAvailableContracts() {
        return Stream.of(Contract.values()).collect(Collectors.toSet());
    }

    public boolean isSelfEmploymentOfContract() {
        String contract = employerDto.getContract();
        if (contract != null) {
            return contract.equals("SELF_EMPLOYMENT");
        }
        return false;
    }
}
