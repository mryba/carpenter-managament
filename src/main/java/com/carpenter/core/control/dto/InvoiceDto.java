package com.carpenter.core.control.dto;

import com.carpenter.core.entity.dictionaries.PaymentType;
import com.carpenter.core.entity.dictionaries.invoice.InvoiceAmountType;
import com.carpenter.core.entity.dictionaries.invoice.InvoiceType;
import com.carpenter.core.entity.dictionaries.invoice.VatRate;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;
import java.util.stream.Stream;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceDto {

    private Long id;
    private String numberOfInvoice;
    private EmployeeDto employeeDto;
    private ClientDto clientDto;
    private BigDecimal netValue;
    private BigDecimal grossValue;
    private InvoiceType invoiceType;
    private Date createDate;
    private String description;

    private BigDecimal vatRate;
    private PaymentType paymentType;
    private LocalDate paymentDue = LocalDate.now().with(TemporalAdjusters.firstDayOfNextMonth());
    private InvoiceAmountType invoiceAmountType;
    private String placeOfCreation;

    public VatRate of(BigDecimal vatRate) {
        return Stream.of(VatRate.values()).filter(vr -> vr.getRate().equals(vatRate)).findFirst().orElse(VatRate.ZERO);
    }

    public String getPlaceOfCreationWithDate() {
        return placeOfCreation + " " + createDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toString();
    }

    public String getEmployeeFullAddress() {
        if (employeeDto.getStreet() == null && employeeDto.getStreetNumber() == null) {
            return employeeDto.getCity() + " " + employeeDto.getHouseNumber();
        }
        return this.employeeDto.getCity() + ", " + this.employeeDto.getStreet() + " " + this.employeeDto.getStreetNumber() + "/" + this.employeeDto.getHouseNumber();
    }

    public String getClientFullAddress() {
        return "ul. " + this.clientDto.getStreet() + " " + this.clientDto.getStreetNumber() + "/" + this.clientDto.getHouseNumber();
    }
}
