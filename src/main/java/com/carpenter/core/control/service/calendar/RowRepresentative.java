package com.carpenter.core.control.service.calendar;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.concurrent.atomic.AtomicInteger;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RowRepresentative {

    private Long employeeId;
    private String employeeName;
    private String employeeLastName;
    private AtomicInteger hours;
}
