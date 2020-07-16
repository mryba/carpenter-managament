package com.carpenter.core.control.service.offer;

import com.carpenter.core.control.dto.OfferDto;
import com.carpenter.core.control.service.login.PrincipalBean;
import com.carpenter.core.entity.Company;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;

@Named("offerListBean")
@ViewScoped
public class OfferListBean implements Serializable {

    @Inject
    OfferService offerService;

    @Inject
    PrincipalBean principalBean;

    private List<OfferDto> offers;

    public Long getUnreadOffersFromLoggedUserCompany() {
        getOffersFromLoggedUserCompany();
        if (offers == null || offers.isEmpty()) {
            return null;
        }
        return offers.stream().filter(o -> !o.getRead()).count();
    }

    public List<OfferDto> getOffersFromLoggedUserCompany() {
        Company company = principalBean.getLoggedUser().getCompany();
        if (company == null) {
            return Collections.emptyList();
        }
        offers = offerService.getOffersByCompany(company.getId());
        return offers;
    }

    public void changeToRead(Long id) {
        offerService.changeToRead(id);
    }

}
