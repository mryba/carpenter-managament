package com.carpenter.api.request;

import lombok.Data;

@Data
public class CarpenterOfferRequest {

    private String name;
    private String email;
    private String company;
    private String phone;
    private String archType;
    private String description;
    private String type;
    private String token;
}
