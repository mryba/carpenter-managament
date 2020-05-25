package com.carpenter.core.control.service.offer;

import com.carpenter.core.control.dto.OfferDto;
import com.carpenter.core.entity.Offer;
import com.carpenter.core.entity.dictionaries.ArchitectureType;
import com.carpenter.utils.DateUtils;
import com.carpenter.utils.Mapper;

import java.util.Date;

public class OfferMapper implements Mapper<Offer, OfferDto> {

    @Override
    public Offer mapFromDomain(OfferDto offerDto) {
        Date dateFrom = DateUtils.convertToDate(offerDto.getWorkDateFrom());
        Date dateTo = DateUtils.convertToDate(offerDto.getWorkDateTo());
        Offer offer = Offer.builder()
                .architectureType(ArchitectureType.valueOf(offerDto.getArchitectureType()))
                .buildingDimension(offerDto.getBuildingDimension())
                .email(offerDto.getEmail())
                .isRead(Boolean.FALSE)
                .phone(offerDto.getPhone())
                .workCity(offerDto.getWorkCity())
                .workDateFrom(dateFrom)
                .workDateTo(dateTo)
                .build();
        return offer;
    }

    @Override
    public OfferDto mapToDomain(Offer offer) {
        return null;
    }
}
