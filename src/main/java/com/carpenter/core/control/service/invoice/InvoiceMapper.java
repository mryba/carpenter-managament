package com.carpenter.core.control.service.invoice;

import com.carpenter.core.control.dto.ClientDto;
import com.carpenter.core.control.dto.EmployeeDto;
import com.carpenter.core.control.dto.InvoiceDto;
import com.carpenter.core.control.service.client.ClientMapper;
import com.carpenter.core.control.service.employee.EmployeeMapper;
import com.carpenter.core.entity.client.Client;
import com.carpenter.core.entity.dictionaries.invoice.InvoiceAmountType;
import com.carpenter.core.entity.employee.Address;
import com.carpenter.core.entity.invoice.Invoice;
import com.carpenter.utils.Mapper;

import javax.ws.rs.client.ClientBuilder;
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
                .placeOfCreation(invoiceDto.getPlaceOfCreation())
                .build();
        invoice.setCreateDate(invoiceDto.getCreateDate());
        return invoice;
    }

    @Override
    public InvoiceDto mapToDomain(Invoice invoice) {

        EmployeeDto employeeDto = EmployeeDto.builder()
                .id(invoice.getEmployee().getId())
                .firstName(invoice.getEmployee().getFirstName())
                .lastName(invoice.getEmployee().getLastName())
                .nipNumber(invoice.getEmployee().getNipNumber())
                .bankAccountNumber(invoice.getEmployee().getBankAccountNumber())
                .city(invoice.getEmployee().getAddresses().stream().map(Address::getCity).findAny().orElse(""))
                .street(invoice.getEmployee().getAddresses().stream().map(Address::getStreet).findAny().orElse(""))
                .streetNumber(invoice.getEmployee().getAddresses().stream().map(Address::getStreetNumber).findAny().orElse(""))
                .houseNumber(invoice.getEmployee().getAddresses().stream().map(Address::getHouseNumber).findAny().orElse(""))
                .build();

        ClientDto clientDto = ClientDto.builder()
                .id(invoice.getClient().getId())
                .name(invoice.getClient().getName())
                .phoneNumber(invoice.getClient().getPhoneNumber())
                .nip(invoice.getClient().getNip())
                .bankAccountNumber(invoice.getClient().getBankAccountNumber())
                .city(invoice.getClient().getCity())
                .street(invoice.getClient().getStreet())
                .streetNumber(invoice.getClient().getStreetNumber())
                .houseNumber(invoice.getClient().getHouseNumber())
                .build();
        return InvoiceDto.builder()
                .id(invoice.getId())
                .employeeDto(employeeDto)
                .clientDto(clientDto)
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
                .placeOfCreation(invoice.getPlaceOfCreation())
                .build();
    }
}
