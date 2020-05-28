package com.carpenter.core.control.service.offer;

import com.carpenter.core.control.dto.OfferDto;
import com.carpenter.core.control.service.company.CompanyService;
import com.carpenter.core.control.service.login.PrincipalBean;
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
import java.util.Date;
import java.util.List;

@Named("offerListBean")
@ViewScoped
public class OfferListBean implements Serializable {

    @Inject
    OfferService offerService;

    @Inject
    CompanyService companyService;

    @Inject
    PrincipalBean principalBean;

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
        offer.setCreateDate(new Date());
        offerService.save(offer);
    }

    public void remove(Long id) {
        offerService.remove(id);
    }

    public List<Offer> getOffersFromLoggedUserCompany() {
        Company company = principalBean.getLoggedUser().getCompany();
        if (company == null) {
            return null;
        }
        return offerService.getOffersByCompany(company.getId());
    }

    public Long getUnreadOffersFromLoggedUserCompany() {
        List<Offer> offers = getOffersFromLoggedUserCompany();
        if (offers == null || offers.isEmpty()) {
            return null;
        }
        return offers.stream().filter(o -> !o.getIsRead()).count();
    }

    public void changeToRead(Long id) {
        offerService.changeToRead(id);
    }

    public ArchitectureType[] getArchitectureTypes() {
        return ArchitectureType.values();
    }
}
