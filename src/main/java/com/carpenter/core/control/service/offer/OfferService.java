package com.carpenter.core.control.service.offer;

import com.carpenter.core.control.dto.OfferDto;
import com.carpenter.core.control.repository.OfferRepository;
import com.carpenter.core.entity.Offer;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import java.io.Serializable;

@SessionScoped
public class OfferService implements Serializable {

    @Inject
    OfferRepository offerRepository;

    private OfferMapper offerMapper;

    public Offer create(OfferDto offerDto) {
        offerMapper = new OfferMapper();
        return offerMapper.mapFromDomain(offerDto);
    }

    public void save(Offer offer) {
        offerRepository.save(offer);
    }
}
