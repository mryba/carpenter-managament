package com.carpenter.core.control.service.invoice;

import com.carpenter.core.control.dto.InvoiceDto;
import com.carpenter.core.entity.invoice.Invoice;
import com.carpenter.utils.ConstantsRegex;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@SessionScoped
public class InvoiceService implements Serializable {

    private static final long serialVersionUID = 4917576989653605758L;
    @Inject
    private InvoiceRepository invoiceRepository;

    private InvoiceMapper invoiceMapper;

    public List<InvoiceDto> getAllInvoices() {
        invoiceMapper = new InvoiceMapper();

        List<Invoice> allInvoices = invoiceRepository.findAllInvoices();
        return allInvoices.stream().map(invoiceMapper::mapToDomain).collect(Collectors.toList());
    }

    public InvoiceNumber getEmployeeLastInvoiceNumber(Long employeeId) {
        String invoiceNumber = invoiceRepository.findLastEmployeeInvoiceNumber(employeeId);
        if (invoiceNumber != null) {
            String[] spitedInvoiceNumber = invoiceNumber.split("/");
            return new InvoiceNumber(Integer.valueOf(spitedInvoiceNumber[0]), Integer.valueOf(spitedInvoiceNumber[1]));

        }
        return null;
    }

    public Invoice createInvoice(InvoiceDto invoiceDto) {
        invoiceMapper = new InvoiceMapper();
        return invoiceMapper.mapFromDomain(invoiceDto);

    }

    public void saveNewInvoice(Invoice invoice) {
        invoiceRepository.persist(invoice);
    }
}
