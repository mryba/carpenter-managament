package com.carpenter.core.control.dto;

import com.carpenter.core.entity.dictionaries.InvoiceType;
import com.carpenter.core.entity.dictionaries.PaymentType;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
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

    private BigDecimal vatRate;
    private PaymentType paymentType;
    private LocalDate paymentDue = LocalDate.now().with(TemporalAdjusters.firstDayOfNextMonth());
}
