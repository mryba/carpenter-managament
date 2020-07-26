package com.carpenter.core.entity.invoice;

import com.carpenter.core.entity.DomainObject;
import com.carpenter.core.entity.client.Client;
import com.carpenter.core.entity.dictionaries.InvoiceType;
import com.carpenter.core.entity.employee.Employee;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Past;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "INVOICE")
@Access(AccessType.FIELD)
@Getter
@Setter
public class Invoice extends DomainObject {

    private static final long serialVersionUID = 4447366168761136L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EMPLOYEE_ID")
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CLIENT_ID")
    private Client client;

    @Column(name = "AMOUNT")
    private BigDecimal amount;

    @Column(name = "INVOICE_TYPE")
    @Enumerated(value = EnumType.STRING)
    private InvoiceType invoiceType;

    @Column(name = "DATE_OF_INVOICE")
    @Past
    private Date dateOfPresent;

    @Column(name = "DESCRIPTION")
    private String description;
}
