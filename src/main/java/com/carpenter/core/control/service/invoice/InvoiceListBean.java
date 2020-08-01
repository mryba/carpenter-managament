package com.carpenter.core.control.service.invoice;

import com.carpenter.core.control.dto.ClientDto;
import com.carpenter.core.control.dto.EmployeeDto;
import com.carpenter.core.control.dto.InvoiceDto;
import com.carpenter.core.control.service.client.ClientService;
import com.carpenter.core.control.service.employee.EmployeeService;
import com.carpenter.core.control.service.login.PrincipalBean;
import com.carpenter.core.entity.dictionaries.invoice.InvoiceAmountType;
import com.carpenter.core.entity.dictionaries.PaymentType;
import com.carpenter.core.entity.dictionaries.invoice.InvoiceType;
import com.carpenter.core.entity.dictionaries.invoice.VatRate;
import com.carpenter.core.entity.invoice.Invoice;
import lombok.Getter;

import javax.annotation.PostConstruct;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@ViewScoped
@Named("invoiceListBean")
public class InvoiceListBean implements Serializable {

    @Inject
    InvoiceService invoiceService;

    @Inject
    EmployeeService employeeService;

    @Inject
    ClientService clientService;

    @Inject
    PrincipalBean principalBean;

    private InvoiceDto invoiceDto;
    private Long invoiceEmployeeId;
    private Long invoiceClientId;

    private List<EmployeeDto> employees;
    private List<ClientDto> clients;

    public void cleanInvoice() {
        invoiceDto = new InvoiceDto();
        invoiceEmployeeId = null;
        invoiceClientId = null;
    }

    public void saveInvoice() {
        InvoiceDto invoiceDto = this.invoiceDto;
        invoiceDto.setDateOfInvoice(new Date());

        Invoice invoice = invoiceService.createInvoice(invoiceDto);
        invoice.setCreateDate(new Date());
        invoice.setCreateBy(principalBean.getLoggedUser().getEmail());

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
            InvoiceNumber newInvoiceNumber;
            InvoiceNumber invoiceNumber = invoiceService.getEmployeeLastInvoiceNumber(employeeId);
            int year = LocalDate.now().getYear();
            if (invoiceNumber != null) {

                Integer invoiceLastYear = invoiceNumber.getYear();
                if (invoiceLastYear.equals(year)) {

                    Integer number = invoiceNumber.getNumber();
                    Integer newNumber = number + 1;
                    newInvoiceNumber = new InvoiceNumber(newNumber, year);
                    invoiceDto.setNumberOfInvoice(newInvoiceNumber.numberOfInvoice());
                    return newInvoiceNumber;
                } else {
                    newInvoiceNumber = new InvoiceNumber(1, year);
                    invoiceDto.setNumberOfInvoice(newInvoiceNumber.numberOfInvoice());
                    return invoiceNumber;
                }
            } else {
                newInvoiceNumber = new InvoiceNumber(1, year);
                invoiceDto.setNumberOfInvoice(newInvoiceNumber.numberOfInvoice());
                return newInvoiceNumber;
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

    public VatRate[] getVatRates() {
        return VatRate.values();
    }

    public PaymentType[] getPaymentTypes() {
        return PaymentType.values();
    }

    public InvoiceType[] getInvoiceTypes() {
        return InvoiceType.values();
    }

}
