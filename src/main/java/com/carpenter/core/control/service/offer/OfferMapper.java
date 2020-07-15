package com.carpenter.core.control.service.offer;

import com.carpenter.core.control.dto.OfferDto;
import com.carpenter.core.entity.Offer;
import com.carpenter.core.entity.dictionaries.ArchitectureType;
import com.carpenter.utils.Mapper;

public class OfferMapper implements Mapper<Offer, OfferDto> {

    @Override
    public Offer mapFromDomain(OfferDto offerDto) {
        return Offer.builder()
                .architectureType(ArchitectureType.of(offerDto.getArchitectureType()))
                .workCity(offerDto.getWorkCity())
                .workDateFrom(offerDto.getWorkDateFrom())
                .forenameOfCalling(offerDto.getForenameOfCalling())
                .companyName(offerDto.getCompanyName())
                .email(offerDto.getEmail())
                .phone(offerDto.getPhone())
                .description(offerDto.getDescription())
                .isRead(Boolean.FALSE)
                .build();
    }

    @Override
    public OfferDto mapToDomain(Offer offer) {
        return OfferDto.builder()
                .architectureType(offer.getArchitectureType().name())
                .workCity(offer.getWorkCity())
                .workDateFrom(offer.getWorkDateFrom())
                .forenameOfCalling(offer.getForenameOfCalling())
                .email(offer.getEmail())
                .phone(offer.getPhone())
                .description(offer.getDescription())
                .build();
    }
}
