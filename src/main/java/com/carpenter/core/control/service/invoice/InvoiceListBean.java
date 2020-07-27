package com.carpenter.core.control.service.invoice;

import com.carpenter.core.control.dto.ClientDto;
import com.carpenter.core.control.dto.EmployeeDto;
import com.carpenter.core.control.dto.InvoiceDto;
import com.carpenter.core.control.service.client.ClientService;
import com.carpenter.core.control.service.employee.EmployeeService;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@ViewScoped
@Named("invoiceListBean")
public class InvoiceListBean implements Serializable {

    @Inject
    InvoiceService invoiceService;

    @Inject
    EmployeeService employeeService;

    @Inject
    ClientService clientService;

    private InvoiceDto invoiceDto;
    private Long invoiceEmployeeId;
    private Long invoiceClientId;

    private List<EmployeeDto> employees;
    private List<ClientDto> clients;


    @PostConstruct
    public void init() {
        invoiceDto = new InvoiceDto();
    }

    public InvoiceDto getInvoiceDto() {
        return invoiceDto;
    }

    public void setInvoiceDto(InvoiceDto invoiceDto) {
        this.invoiceDto = invoiceDto;
    }

    public List<InvoiceDto> getInvoicesList() {
        return invoiceService.getAllInvoices();
    }


    public Long getInvoiceEmployeeId() {
        return invoiceEmployeeId;
    }

    public void setInvoiceEmployeeId(Long invoiceEmployeeId) {
        this.invoiceEmployeeId = invoiceEmployeeId;
        employees.stream()
                .filter(e -> e.getId().equals(invoiceEmployeeId))
                .findFirst()
                .ifPresent(employeeDto -> invoiceDto.setEmployeeDto(employeeDto));
    }

    public Long getInvoiceClientId() {
        return invoiceClientId;
    }

    public void setInvoiceClientId(Long invoiceClientId) {
        this.invoiceClientId = invoiceClientId;
        clients.stream().filter(c -> c.getId().equals(invoiceClientId))
                .findFirst()
                .ifPresent(clientDto -> invoiceDto.setClientDto(clientDto));
    }

    public List<EmployeeDto> getEmployeesList() {
        employees = employeeService.getEmployees();
        return employees;
    }

    public InvoiceNumber getInvoiceNumber() {
        if (invoiceDto != null && invoiceDto.getEmployeeDto() != null) {

            Long employeeId = invoiceDto.getEmployeeDto().getId();

            InvoiceNumber invoiceNumber = invoiceService.getEmployeeLastInvoiceNumber(employeeId);
            if (invoiceNumber != null) {
                return invoiceNumber;
            }
        }
        return null;
    }

    public List<ClientDto> getClientList() {
        clients = clientService.getAllAvailableClients();
        return clients;
    }

    public void calculateGrossValue() {
        if (invoiceDto.getGrossValue() != null) {
            BigDecimal grossValue = invoiceDto.getGrossValue();
            BigDecimal netValue = grossValue.multiply(new BigDecimal(25).divide(new BigDecimal(100)));
            invoiceDto.setGrossValue(netValue);
        } else {
            invoiceDto.setNetValue(null);
        }
    }
}
