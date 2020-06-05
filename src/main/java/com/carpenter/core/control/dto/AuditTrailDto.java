package com.carpenter.core.control.dto;

import com.carpenter.core.entity.employee.Employee;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuditTrailDto {

    private Long id;
    private String activity;
    private Employee employee;
}
