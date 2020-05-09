package com.carpenter.core.control.service.company;

import com.carpenter.core.entity.Company;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import java.io.Serializable;

@Slf4j
@SessionScoped
public class CompanyService implements Serializable {

    @Inject
    CompanyRepository companyRepository;

    public Company getCompanyById(Long companyId) {
       return companyRepository.findCompanyById(companyId);
    }
}
