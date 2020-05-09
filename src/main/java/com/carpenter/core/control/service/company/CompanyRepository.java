package com.carpenter.core.control.service.company;

import com.carpenter.core.entity.Company;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;

@Stateless
public class CompanyRepository implements Serializable {

    @PersistenceContext
    EntityManager entityManager;

    public Company findCompanyById(Long companyId) {
       return entityManager.find(Company.class, companyId);
    }
}
