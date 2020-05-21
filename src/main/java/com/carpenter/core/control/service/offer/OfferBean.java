package com.carpenter.core.control.service.offer;

import com.carpenter.core.control.dto.OfferDto;
import com.carpenter.core.control.service.company.CompanyService;
import com.carpenter.core.entity.Company;
import com.carpenter.core.entity.Offer;
import com.carpenter.core.entity.dictionaries.ArchitectureType;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

@Named("offerBean")
@ViewScoped
public class OfferBean implements Serializable {

    @Inject
    OfferService offerService;

    @Inject
    CompanyService companyService;

    @Getter
    @Setter
    private OfferDto offerDto;

    @PostConstruct
    public void init() {
        offerDto = new OfferDto();
    }

    public void save() {
        Offer offer = offerService.create(offerDto);
        //TODO przypisać odpowiednią firmę do oferty. Tymczasowo :
        Company company = companyService.getCompanyById(1L);
        if (company == null) {
            return;
        }
        offer.setCompany(company);
        offerService.save(offer);
    }

    public ArchitectureType[] getArchitectureTypes() {
        return ArchitectureType.values();
    }
}
