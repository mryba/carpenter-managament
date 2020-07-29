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
            String[] splitedInvoiceNumber = invoiceNumber.split(ConstantsRegex.INVOICE_NUMBER_PATTERN);
            InvoiceNumber in =
                    new InvoiceNumber(Integer.valueOf(splitedInvoiceNumber[0]), Integer.valueOf(splitedInvoiceNumber[1]));
            return in;
        }
        return null;
    }

    public Invoice createInvoice(InvoiceDto invoiceDto) {
        invoiceMapper = new InvoiceMapper();
        return invoiceMapper.mapFromDomain(invoiceDto);

    }
}
