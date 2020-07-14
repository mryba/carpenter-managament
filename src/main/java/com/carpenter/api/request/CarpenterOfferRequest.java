package com.carpenter.api.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class CarpenterOfferRequest {

    private String city;
//    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ", shape = JsonFormat.Shape.STRING)
    private String startDate;
    private String name;
    private String email;
    private String company;
    private String phone;
    private String archType;
    private String description;
    private String type;
    private String token;
}
