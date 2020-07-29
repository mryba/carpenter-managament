package com.carpenter.core.control.service.invoice;

import com.carpenter.core.control.dto.InvoiceDto;
import com.carpenter.core.control.service.client.ClientMapper;
import com.carpenter.core.control.service.employee.EmployeeMapper;
import com.carpenter.core.entity.invoice.Invoice;
import com.carpenter.utils.Mapper;

import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;

public class InvoiceMapper implements Mapper<Invoice, InvoiceDto> {

    @Override
    public Invoice mapFromDomain(InvoiceDto invoiceDto) {
        EmployeeMapper employeeMapper = new EmployeeMapper();
        ClientMapper clientMapper = new ClientMapper();

        return Invoice.builder()
                .numberOfInvoice(invoiceDto.getNumberOfInvoice())
                .employee(employeeMapper.mapFromDomain(invoiceDto.getEmployeeDto()))
                .client(clientMapper.mapFromDomain(invoiceDto.getClientDto()))
                .invoiceAmountType(invoiceDto.getInvoiceAmountType())
                .netValue(invoiceDto.getNetValue())
                .grossValue(invoiceDto.getGrossValue())
                .invoiceType(invoiceDto.getInvoiceType())
                .dateOfInvoice(invoiceDto.getDateOfInvoice())
                .vatRate(invoiceDto.getVatRate())
                .paymentType(invoiceDto.getPaymentType())
                .paymentDue(Date.from(invoiceDto.getPaymentDue().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()))
                .description(invoiceDto.getDescription())
                .build();
    }

    @Override
    public InvoiceDto mapToDomain(Invoice invoice) {
        EmployeeMapper employeeMapper = new EmployeeMapper();
        ClientMapper clientMapper = new ClientMapper();
        return InvoiceDto.builder()
                .id(invoice.getId())
                .employeeDto(employeeMapper.mapToDomain(invoice.getEmployee()))
                .clientDto(clientMapper.mapToDomain(invoice.getClient()))
                .invoiceAmountType(invoice.getInvoiceAmountType())
                .netValue(invoice.getNetValue())
                .grossValue(invoice.getGrossValue())
                .invoiceType(invoice.getInvoiceType())
                .dateOfInvoice(invoice.getDateOfInvoice())
                .vatRate(invoice.getVatRate())
                .paymentType(invoice.getPaymentType())
                .paymentDue(invoice.getPaymentDue().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
                .description(invoice.getDescription())
                .build();
    }
}
