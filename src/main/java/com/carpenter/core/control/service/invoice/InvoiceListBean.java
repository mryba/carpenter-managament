package com.carpenter.core.control.service.invoice;

import com.carpenter.core.control.dto.EmployeeDto;
import com.carpenter.core.control.dto.InvoiceDto;
import com.carpenter.core.control.service.employee.EmployeeService;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@ViewScoped
@Named("invoiceListBean")
public class InvoiceListBean implements Serializable {

    @Inject
    InvoiceService invoiceService;

    @Inject
    EmployeeService employeeService;
    
    private InvoiceDto invoiceDto;

    public List<InvoiceDto> getInvoicesList() {
        return invoiceService.getAllInvoices();
    }

    public InvoiceDto getInvoiceDto() {
        return invoiceDto;
    }

    public void setInvoiceDto(InvoiceDto invoiceDto) {
        this.invoiceDto = invoiceDto;
    }

    public List<EmployeeDto> getEmployeesList() {
        return employeeService.getEmployees();
    }
}
