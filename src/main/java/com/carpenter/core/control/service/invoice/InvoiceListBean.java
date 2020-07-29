package com.carpenter.core.control.service.invoice;

import com.carpenter.core.control.dto.ClientDto;
import com.carpenter.core.control.dto.EmployeeDto;
import com.carpenter.core.control.dto.InvoiceDto;
import com.carpenter.core.control.service.client.ClientService;
import com.carpenter.core.control.service.employee.EmployeeService;
import com.carpenter.core.entity.dictionaries.invoice.InvoiceAmountType;
import com.carpenter.core.entity.dictionaries.PaymentType;
import com.carpenter.core.entity.dictionaries.invoice.InvoiceType;
import com.carpenter.core.entity.dictionaries.invoice.VatRate;

import javax.annotation.PostConstruct;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    public void cleanInvoice() {
        invoiceDto = null;
        invoiceEmployeeId = null;
        invoiceClientId = null;
    }

    public void saveInvoice() {
        InvoiceDto invoiceDto = this.invoiceDto;
        cleanInvoice();
    }

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

    public void calculateNetValue() {
        if (invoiceDto.getGrossValue() != null) {
            if (!invoiceDto.getVatRate().equals(VatRate.ZERO.getRate())) {
                BigDecimal vatRate = invoiceDto.getVatRate().add(BigDecimal.ONE);
                BigDecimal netValue = invoiceDto.getGrossValue().setScale(2, RoundingMode.HALF_EVEN).divide(vatRate, RoundingMode.HALF_UP).setScale(2, RoundingMode.HALF_UP);
                invoiceDto.setNetValue(netValue);
            }
        } else {
            invoiceDto.setNetValue(null);
        }
    }

    public void calculateGrossValue() {
        if (invoiceDto.getNetValue() != null) {
            BigDecimal grossValue =
                    invoiceDto.getNetValue().add(invoiceDto.getNetValue().multiply(invoiceDto.getVatRate())).setScale(2, RoundingMode.HALF_EVEN);
            invoiceDto.setGrossValue(grossValue);
        } else {
            invoiceDto.setGrossValue(null);
        }
    }


    public InvoiceAmountType[] getAmountTypes() {
        return InvoiceAmountType.values();
    }

    public List<VatRate> getVatRates() {
        return Stream.of(VatRate.values()).collect(Collectors.toList());
    }

    public List<PaymentType> getPaymentTypes() {
        return Stream.of(PaymentType.values()).collect(Collectors.toList());
    }

    public InvoiceType[] getInvoiceTypes() {
        return InvoiceType.values();
    }
}
