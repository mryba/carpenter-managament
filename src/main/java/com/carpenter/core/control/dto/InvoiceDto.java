package com.carpenter.core.control.dto;

import com.carpenter.core.entity.dictionaries.InvoiceType;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceDto {

    private Long id;
    private EmployeeDto employeeDto;
    private ClientDto clientDto;
    private BigDecimal netValue;
    private BigDecimal grossValue;
    private InvoiceType invoiceType;
    private Date dateOfInvoice;
    private String description;
}
