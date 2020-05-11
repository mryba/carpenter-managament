package com.carpenter.core.control.service.company;

import com.carpenter.core.control.dto.CompanyDto;
import com.carpenter.core.entity.Company;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;

@Stateless
public class CompanyRepository implements Serializable {

    @PersistenceContext
    EntityManager entityManager;

    public Company findCompanyById(Long companyId) {
       return entityManager.find(Company.class, companyId);
    }

    public List<CompanyDto> findAllActiveCompanies() {
        try {
            return entityManager.createNamedQuery("Company.findAllActiveCompanies", CompanyDto.class)
                    .getResultList();
        }catch (NoResultException e){
            return Collections.emptyList();
        }
    }
}
