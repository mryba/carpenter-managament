package com.carpenter.core.control.service.invoice;

import com.carpenter.core.control.dto.ClientDto;
import com.carpenter.core.control.dto.EmployeeDto;
import com.carpenter.core.control.dto.InvoiceDto;
import com.carpenter.core.control.pdf.PdfService;
import com.carpenter.core.control.service.client.ClientService;
import com.carpenter.core.control.service.employee.EmployeeService;
import com.carpenter.core.control.service.login.PrincipalBean;
import com.carpenter.core.entity.client.Client;
import com.carpenter.core.entity.dictionaries.PaymentType;
import com.carpenter.core.entity.dictionaries.invoice.InvoiceAmountType;
import com.carpenter.core.entity.dictionaries.invoice.InvoiceType;
import com.carpenter.core.entity.dictionaries.invoice.VatRate;
import com.carpenter.core.entity.employee.Employee;
import com.carpenter.core.entity.invoice.Invoice;
import lombok.Getter;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Getter
@ViewScoped
@Named("invoiceListBean")
public class InvoiceListBean implements Serializable {

    private static final long serialVersionUID = -7823117445786511544L;

    @Inject
    InvoiceService invoiceService;

    @Inject
    EmployeeService employeeService;

    @Inject
    ClientService clientService;

    @Inject
    PrincipalBean principalBean;

    @Inject
    PdfService pdfService;

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
        Invoice invoice = invoiceService.createInvoice(invoiceDto);
        invoice.setCreateDate(new Date());
        invoice.setCreateBy(principalBean.getLoggedUser().getEmail());

        Client client = clientService.getClientById(invoiceClientId);
        client.addInvoice(invoice);
        invoice.setClient(client);

        Employee employee = employeeService.getEmployeeById(invoiceEmployeeId);
        employee.addInvoice(invoice);
        invoice.setEmployee(employee);

        invoiceService.saveNewInvoice(invoice);
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
        return invoiceService.getAllInvoices(principalBean);
    }

    public Long getInvoiceEmployeeId() {
        return invoiceEmployeeId;
    }

    public void setInvoiceEmployeeId(Long invoiceEmployeeId) {
        this.invoiceEmployeeId = invoiceEmployeeId;
        employees.stream()
                .filter(e -> e.getId().equals(invoiceEmployeeId))
                .findFirst()
                .ifPresent(e -> {
                    invoiceDto.setEmployeeDto(new EmployeeDto());
                    invoiceDto.getEmployeeDto().setId(e.getId());
                    invoiceDto.getEmployeeDto().setFirstName(e.getFirstName());
                    invoiceDto.getEmployeeDto().setLastName(e.getLastName());
                    invoiceDto.getEmployeeDto().setNipNumber(e.getNipNumber());
                    invoiceDto.getEmployeeDto().setBankAccountNumber(e.getBankAccountNumber());
                    invoiceDto.getEmployeeDto().setCity(e.getCity());
                    invoiceDto.getEmployeeDto().setStreet(e.getStreet());
                    invoiceDto.getEmployeeDto().setStreetNumber(e.getStreetNumber());
                    invoiceDto.getEmployeeDto().setHouseNumber(e.getHouseNumber());
                });
    }

    public Long getInvoiceClientId() {
        return invoiceClientId;
    }

    public void setInvoiceClientId(Long invoiceClientId) {
        this.invoiceClientId = invoiceClientId;
        clients.stream().filter(c -> c.getId().equals(invoiceClientId))
                .findFirst()
                .ifPresent(c -> {
                    invoiceDto.setClientDto(new ClientDto());
                    invoiceDto.getClientDto().setId(c.getId());
                    invoiceDto.getClientDto().setName(c.getName());
                    invoiceDto.getClientDto().setBankAccountNumber(c.getBankAccountNumber());
                    invoiceDto.getClientDto().setNip(c.getNip());
                    invoiceDto.getClientDto().setCity(c.getCity());
                    invoiceDto.getClientDto().setStreet(c.getStreet());
                    invoiceDto.getClientDto().setStreetNumber(c.getStreetNumber());
                    invoiceDto.getClientDto().setHouseNumber(c.getHouseNumber());
                });
    }

    public List<EmployeeDto> getEmployeesList() {
        employees = employeeService.getAllActiveEmployees(employeeService.getAllActiveSelfEmploymentEmployees(principalBean));
        return employees;
    }

    public InvoiceNumber getInvoiceNumber() {
        if (invoiceDto != null && invoiceDto.getEmployeeDto().getId() != null) {

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
                    return newInvoiceNumber;
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
        clients = clientService.getAllAvailableClients(principalBean);
        return clients;
    }

    public void calculateNetValue() {
        if (invoiceDto.getGrossValue() != null) {
            if (invoiceDto.getVatRate().compareTo(BigDecimal.ZERO) > 0) {
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


    public List<InvoiceAmountType> getAmountTypes() {
        return List.of(InvoiceAmountType.GROSS, InvoiceAmountType.NET);
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

    public void renderPdf(InvoiceDto dto) {
        pdfService.renderPdf(dto);
    }

}
