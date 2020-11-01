package com.carpenter.core.control.service.offer;

import com.carpenter.core.control.dto.OfferDto;
import com.carpenter.core.entity.Offer;
import org.omnifaces.cdi.Param;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

@Named("offerBean")
@ViewScoped
public class OfferBean implements Serializable {

    private static final long serialVersionUID = -6348629792592013131L;

    @Inject
    @Param(name = "offerId")
    private Long offerId;

    private OfferDto offerDto;
    private OfferMapper offerMapper;

    @Inject
    OfferService offerService;

    @PostConstruct
    public void init() {
        if (offerId != null) {
            offerMapper = new OfferMapper();
            Offer offer = offerService.findOfferById(offerId);
            offerDto = offerMapper.mapToDomain(offer);
        }
    }

    public OfferDto getOfferDto() {
        return offerDto;
    }

    public Long getOfferId() {
        return offerId;
    }
}
