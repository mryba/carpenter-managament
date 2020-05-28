package com.carpenter.core.control.service.offer;

import com.carpenter.core.control.dto.OfferDto;
import com.carpenter.core.control.repository.OfferRepository;
import com.carpenter.core.entity.Offer;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.List;

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

    public void remove(Long id) {
        offerRepository.remove(id);
    }

    public void changeToRead(Long id) {
        offerRepository.changeToRead(id);
    }

    public List<Offer> getOffersByCompany(Long companyId) {
        if (companyId == null) {
            return null;
        }
        return offerRepository.getOffersByCompany(companyId);
    }

    public Offer findOfferById(Long id) {
        return offerRepository.findOfferById(id);
    }
}
