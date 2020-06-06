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

    //todo w DTO embedded niech bedzie takze DTO - > EmployeeDto
    private Employee employee;
}
