package com.carpenter.core.control.service.invoice;

import com.carpenter.core.control.dto.InvoiceDto;
import com.carpenter.core.entity.invoice.Invoice;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;

@ViewScoped
@Named("invoiceListBean")
public class InvoiceListBean implements Serializable {

    @Inject
    InvoiceService invoiceService;

    public List<InvoiceDto> getInvoicesList() {
        return invoiceService.getAllInvoices();
    }
}
