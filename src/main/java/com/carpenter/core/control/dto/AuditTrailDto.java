package com.carpenter.core.control.dto;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuditTrailDto {

    private long id;
    private String activity;
    private String employeeId;
}
