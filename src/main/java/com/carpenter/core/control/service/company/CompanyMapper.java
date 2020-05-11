package com.carpenter.core.control.service.company;

import com.carpenter.core.control.dto.CompanyDto;
import com.carpenter.core.entity.Company;
import com.carpenter.utils.Mapper;

public class CompanyMapper implements Mapper<Company, CompanyDto> {

    @Override
    public Company mapFromDomain(CompanyDto companyDto) {
        return null;
    }

    @Override
    public CompanyDto mapToDomain(Company company) {
        return null;
    }
}
