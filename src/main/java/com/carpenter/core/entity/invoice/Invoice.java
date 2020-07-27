package com.carpenter.core.entity.invoice;

import com.carpenter.core.entity.DomainObject;
import com.carpenter.core.entity.client.Client;
import com.carpenter.core.entity.dictionaries.InvoiceType;
import com.carpenter.core.entity.employee.Employee;
import com.carpenter.utils.ConstantsRegex;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "INVOICES")
@Access(AccessType.FIELD)
@Getter
@Setter
@NamedQueries({
        @NamedQuery(
                name = "Invoice.findAllInvoices",
                query = "SELECT i FROM Invoice i"
        ),
        @NamedQuery(
                name = "Invoice.findLastEmployeeInvoice",
                query = "SELECT i.numberOfInvoice FROM Invoice i WHERE i.employee.id =:employeeId ORDER BY i.createDate"
        )
})
public class Invoice extends DomainObject {

    private static final long serialVersionUID = 4447366168761136L;

    @Column(name = "NUMBER_OF_INVOICE")
    @NotNull
    @Pattern(regexp = ConstantsRegex.INVOICE_NUMBER_PATTERN)
    private String numberOfInvoice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EMPLOYEE_ID")
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CLIENT_ID")
    private Client client;

    @Column(name = "NET_VALUE")
    private BigDecimal netValue;

    @Column(name = "GROSS_VALUE")
    private BigDecimal grossValue;

    @Column(name = "INVOICE_TYPE")
    @Enumerated(value = EnumType.STRING)
    private InvoiceType invoiceType;

    @Past
    @Column(name = "DATE_OF_INVOICE")
    private Date dateOfInvoice;

    @Column(name = "DESCRIPTION")
    private String description;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Invoice invoice = (Invoice) o;
        return Objects.equals(employee, invoice.employee) &&
                Objects.equals(client, invoice.client) &&
                Objects.equals(netValue, invoice.netValue) &&
                Objects.equals(grossValue, invoice.grossValue) &&
                invoiceType == invoice.invoiceType &&
                Objects.equals(dateOfInvoice, invoice.dateOfInvoice) &&
                Objects.equals(description, invoice.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), employee, client, netValue, grossValue, invoiceType, dateOfInvoice, description);
    }
}
