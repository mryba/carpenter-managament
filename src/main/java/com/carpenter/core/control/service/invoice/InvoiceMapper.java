package com.carpenter.core.control.service.invoice;

import com.carpenter.core.control.dto.InvoiceDto;
import com.carpenter.core.control.service.client.ClientMapper;
import com.carpenter.core.control.service.employee.EmployeeMapper;
import com.carpenter.core.entity.invoice.Invoice;
import com.carpenter.utils.Mapper;

public class InvoiceMapper implements Mapper<Invoice, InvoiceDto> {


    @Override
    public Invoice mapFromDomain(InvoiceDto invoiceDto) {
        return null;
    }

    @Override
    public InvoiceDto mapToDomain(Invoice invoice) {
        EmployeeMapper employeeMapper = new EmployeeMapper();
        ClientMapper clientMapper = new ClientMapper();
        return InvoiceDto.builder()
                .id(invoice.getId())
                .employeeDto(employeeMapper.mapToDomain(invoice.getEmployee()))
                .clientDto(clientMapper.mapToDomain(invoice.getClient()))
                .netValue(invoice.getNetValue())
                .grossValue(invoice.getGrossValue())
                .invoiceType(invoice.getInvoiceType())
                .dateOfInvoice(invoice.getDateOfInvoice())
                .description(invoice.getDescription())
                .build();
    }
}
