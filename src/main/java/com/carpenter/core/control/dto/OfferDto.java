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
    private String architectureType;
    private Date workDateFrom;
    private String forenameOfCalling;
    private String companyName;
    private String phone;
    private String email;
    private String description;

    //Company
    private Long companyId;
}
