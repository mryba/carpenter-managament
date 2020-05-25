package com.carpenter.core.control.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OfferDto {

    //Offer
    private Long id;
    private String workCity;
    private String workDateFrom;
    private String workDateTo;
    private Double buildingDimension;
    private String architectureType;
    private String phone;
    private String email;

    //Company
    private Long companyId;
}
