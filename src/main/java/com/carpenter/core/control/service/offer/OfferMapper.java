package com.carpenter.core.control.service.offer;

import com.carpenter.core.control.dto.OfferDto;
import com.carpenter.core.entity.Offer;
import com.carpenter.core.entity.dictionaries.ArchitectureType;
import com.carpenter.utils.Mapper;

public class OfferMapper implements Mapper<Offer, OfferDto> {

    @Override
    public Offer mapFromDomain(OfferDto offerDto) {
        Offer offer = Offer.builder()
                .architectureType(ArchitectureType.valueOf(offerDto.getArchitectureType()))
                .buildingDimension(offerDto.getBuildingDimension())
                .email(offerDto.getEmail())
                .isRead(Boolean.FALSE)
                .phone(offerDto.getPhone())
                .workCity(offerDto.getWorkCity())
                .workDateFrom(offerDto.getWorkDateFrom())
                .workDateTo(offerDto.getWorkDateTo())
                .build();
        return offer;
    }

    @Override
    public OfferDto mapToDomain(Offer offer) {
        return null;
    }
}
