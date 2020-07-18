package com.carpenter.core.control.service.company;

import com.carpenter.core.control.dto.CompanyDto;
import com.carpenter.core.entity.Company;
import org.hibernate.jpa.QueryHints;

import javax.ejb.Stateless;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import static com.carpenter.utils.ConstantsRegex.FETCH_GRAPH;

@Stateless
public class CompanyRepository implements Serializable {

    @PersistenceContext
    private EntityManager entityManager;

    public Company findCompanyById(Long companyId) {
        try {
            return entityManager.createNamedQuery("Company.findById", Company.class)
                    .setParameter("companyId", companyId)
                    .getResultList().iterator().next();
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    public Company findCompanyByName(String companyName) {
        try {
            return entityManager.createNamedQuery("Company.findByName", Company.class)
                    .setParameter("companyName", companyName)
                    .getResultList().iterator().next();
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    public List<CompanyDto> findAllActiveCompanies() {
        try {
            return entityManager.createNamedQuery("Company.findAllActiveCompanies", CompanyDto.class)
                    .getResultList();
        } catch (NoResultException e) {
            return Collections.emptyList();
        }
    }

    public void save(Company company) {
        entityManager.merge(company);
    }
}
