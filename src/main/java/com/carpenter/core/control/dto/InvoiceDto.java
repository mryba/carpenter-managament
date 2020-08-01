package com.carpenter.core.control.dto;

import com.carpenter.core.entity.dictionaries.invoice.InvoiceAmountType;
import com.carpenter.core.entity.dictionaries.invoice.InvoiceType;
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
    private String numberOfInvoice;

    private Long employeeId;
    private String employeeFirstName;
    private String employeeLastName;
    private String employeeNipNumber;
    private String employeeAccountNumber;

    private Long clientId;
    private String clientName;
    private String clientNipNumber;
    private String clientAccountNumber;

    private BigDecimal netValue;
    private BigDecimal grossValue;
    private InvoiceType invoiceType;
    private Date dateOfInvoice;
    private String description;

    private BigDecimal vatRate;
    private PaymentType paymentType;
    private LocalDate paymentDue = LocalDate.now().with(TemporalAdjusters.firstDayOfNextMonth());
    private InvoiceAmountType invoiceAmountType;
}
