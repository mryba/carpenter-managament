package com.carpenter.core.control.service.company;

import com.carpenter.core.control.dto.CompanyDto;
import com.carpenter.core.entity.Company;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.List;

@Slf4j
@SessionScoped
public class CompanyService implements Serializable {

    @Inject
    CompanyRepository companyRepository;

    public Company getCompanyById(Long companyId) {
       return companyRepository.findCompanyById(companyId);
    }

    public List<CompanyDto> getAllActiveCompanies() {
        return companyRepository.findAllActiveCompanies();
    }
}
