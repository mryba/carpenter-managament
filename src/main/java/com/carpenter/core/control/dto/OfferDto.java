package com.carpenter.core.control.dto;

import lombok.*;

import javax.validation.constraints.Size;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OfferDto {

    //Offer
    private Long id;
    private String workCity;
    private Date workDateFrom;
    private Date workDateTo;
    private Double buildingDimension;
    private String architectureType;
    private String phone;
    private String email;

    //Company
    private Long companyId;
}
