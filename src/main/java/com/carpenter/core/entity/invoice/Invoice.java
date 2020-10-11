package com.carpenter.core.entity.invoice;

import com.carpenter.core.entity.DomainObject;
import com.carpenter.core.entity.client.Client;
import com.carpenter.core.entity.dictionaries.PaymentType;
import com.carpenter.core.entity.dictionaries.invoice.InvoiceAmountType;
import com.carpenter.core.entity.dictionaries.invoice.InvoiceType;
import com.carpenter.core.entity.employee.Employee;
import com.carpenter.utils.ConstantsRegex;
import lombok.*;

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
@AllArgsConstructor
@NoArgsConstructor
@Builder
@NamedQueries({
        @NamedQuery(
                name = "Invoice.findLastEmployeeInvoice",
                query = "SELECT i.numberOfInvoice FROM Invoice i WHERE i.employee.id =:employeeId ORDER BY i.createDate DESC"
        )
})

public class Invoice extends DomainObject {

    private static final long serialVersionUID = 4447366168761136L;

    @Column(name = "NUMBER_OF_INVOICE")
    @NotNull
    @Pattern(regexp = ConstantsRegex.INVOICE_NUMBER_PATTERN)
    private String numberOfInvoice;

    @Column(name = "DATE_OF_CREATION")
    @NotNull
    private Date dateOfCreation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EMPLOYEE_ID")
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CLIENT_ID")
    private Client client;

    @Column(name = "INVOICE_AMOUNT_TYPE")
    @Enumerated(value = EnumType.STRING)
    private InvoiceAmountType invoiceAmountType;

    @Column(name = "NET_VALUE")
    private BigDecimal netValue;

    @Column(name = "GROSS_VALUE")
    private BigDecimal grossValue;

    @Column(name = "INVOICE_TYPE")
    @Enumerated(value = EnumType.STRING)
    private InvoiceType invoiceType;

    @Column(name = "VAT_RATE")
    private BigDecimal vatRate;

    @Column(name = "PAYMENT_TYPE")
    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;

    @Column(name = "PAYMENT_DUE")
    private Date paymentDue;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "PLACE_OF_CREATION")
    private String placeOfCreation;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Invoice)) return false;
        if (!super.equals(o)) return false;
        Invoice invoice = (Invoice) o;
        return Objects.equals(numberOfInvoice, invoice.numberOfInvoice) &&
                Objects.equals(dateOfCreation, invoice.dateOfCreation) &&
                Objects.equals(employee, invoice.employee) &&
                Objects.equals(client, invoice.client) &&
                invoiceAmountType == invoice.invoiceAmountType &&
                Objects.equals(netValue, invoice.netValue) &&
                Objects.equals(grossValue, invoice.grossValue) &&
                invoiceType == invoice.invoiceType &&
                Objects.equals(vatRate, invoice.vatRate) &&
                paymentType == invoice.paymentType &&
                Objects.equals(paymentDue, invoice.paymentDue) &&
                Objects.equals(description, invoice.description) &&
                Objects.equals(placeOfCreation, invoice.placeOfCreation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), numberOfInvoice, dateOfCreation, employee, client, invoiceAmountType, netValue, grossValue, invoiceType, vatRate, paymentType, paymentDue, description, placeOfCreation);
    }
}
