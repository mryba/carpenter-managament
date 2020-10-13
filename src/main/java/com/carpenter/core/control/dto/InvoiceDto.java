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
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
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
    private LocalDate dateOfCreation;

    private BigDecimal vatRate;
    private PaymentType paymentType;
    private LocalDate paymentDue = LocalDate.now().with(TemporalAdjusters.firstDayOfNextMonth());
    private InvoiceAmountType invoiceAmountType;
    private String placeOfCreation;

    public String of(BigDecimal vatRate) {
        if (vatRate != null) {
            return Stream.of(VatRate.values()).filter(vr -> vr.getRate().compareTo(vatRate) == 0).findFirst().orElse(VatRate.NP).toString();
        }else {
            return VatRate.ZERO.name();
        }
    }

    public String getEmployeeFullAddress() {
        if (employeeDto.getStreet() == null && employeeDto.getStreetNumber() == null) {
            return employeeDto.getCity() + " " + employeeDto.getHouseNumber();
        }
        return this.employeeDto.getCity() + ", " + this.employeeDto.getStreet() + " " + this.employeeDto.getStreetNumber() + "/" + this.employeeDto.getHouseNumber();
    }

    public String getClientFullAddress() {
        if ((clientDto.getStreet() == null || clientDto.getStreet().isEmpty()) && clientDto.getStreetNumber() == null) {
            return clientDto.getCity() + " " + clientDto.getHouseNumber();
        }
        return this.clientDto.getCity() + ", " + this.clientDto.getStreet() + " " + this.clientDto.getStreetNumber() + "/" + this.clientDto.getHouseNumber();
//        return "ul. " + this.clientDto.getStreet() + " " + this.clientDto.getStreetNumber() + "/" + this.clientDto.getHouseNumber();
    }

    public Date getDateOfCreationAsDate() {
        return Date.from(this.dateOfCreation.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }
}
