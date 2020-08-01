package com.carpenter.core.control.service.invoice;

import com.carpenter.core.control.dto.InvoiceDto;
import com.carpenter.core.control.service.client.ClientMapper;
import com.carpenter.core.control.service.employee.EmployeeMapper;
import com.carpenter.core.entity.dictionaries.invoice.InvoiceAmountType;
import com.carpenter.core.entity.invoice.Invoice;
import com.carpenter.utils.Mapper;

import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;

public class InvoiceMapper implements Mapper<Invoice, InvoiceDto> {

    @Override
    public Invoice mapFromDomain(InvoiceDto invoiceDto) {
        Invoice invoice = Invoice.builder()
                .numberOfInvoice(invoiceDto.getNumberOfInvoice())
                .invoiceAmountType(invoiceDto.getInvoiceAmountType() != null ? invoiceDto.getInvoiceAmountType() : InvoiceAmountType.NONE)
                .netValue(invoiceDto.getNetValue())
                .grossValue(invoiceDto.getGrossValue())
                .invoiceType(invoiceDto.getInvoiceType())
                .vatRate(invoiceDto.getVatRate())
                .paymentType(invoiceDto.getPaymentType())
                .paymentDue(Date.from(invoiceDto.getPaymentDue().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()))
                .description(invoiceDto.getDescription())
                .build();
        invoice.setCreateDate(invoiceDto.getCreateDate());
        return invoice;
    }

    @Override
    public InvoiceDto mapToDomain(Invoice invoice) {
        return InvoiceDto.builder()
                .id(invoice.getId())
                .employeeId(invoice.getEmployee().getId())
                .employeeFirstName(invoice.getEmployee().getFirstName())
                .employeeLastName(invoice.getEmployee().getLastName())
                .employeeNipNumber(invoice.getEmployee().getNipNumber())
                .employeeAccountNumber(invoice.getEmployee().getBankAccountNumber())
                .clientId(invoice.getClient().getId())
                .clientName(invoice.getClient().getName())
                .clientNipNumber(invoice.getClient().getNip())
                .clientAccountNumber(invoice.getClient().getBankAccountNumber())
                .invoiceAmountType(invoice.getInvoiceAmountType())
                .numberOfInvoice(invoice.getNumberOfInvoice())
                .netValue(invoice.getNetValue())
                .grossValue(invoice.getGrossValue())
                .invoiceType(invoice.getInvoiceType())
                .createDate(invoice.getCreateDate())
                .vatRate(invoice.getVatRate())
                .paymentType(invoice.getPaymentType())
                .paymentDue(invoice.getPaymentDue().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
                .description(invoice.getDescription())
                .build();
    }
}
