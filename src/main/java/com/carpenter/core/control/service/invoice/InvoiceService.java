package com.carpenter.core.control.service.invoice;

import com.carpenter.core.control.dto.InvoiceDto;
import com.carpenter.core.entity.invoice.Invoice;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@SessionScoped
public class InvoiceService implements Serializable {

    @Inject
    private InvoiceRepository invoiceRepository;

    public List<InvoiceDto> getAllInvoices() {
        InvoiceMapper invoiceMapper = new InvoiceMapper();

        List<Invoice> allInvoices = invoiceRepository.findAllInvoices();
        return allInvoices.stream().map(invoiceMapper::mapToDomain).collect(Collectors.toList());
    }
}
