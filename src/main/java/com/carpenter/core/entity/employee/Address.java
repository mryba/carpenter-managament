package com.carpenter.core.entity.employee;

import com.carpenter.core.entity.DomainObject;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlTransient;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "ADDRESSES")
@Access(AccessType.FIELD)
class Address extends DomainObject {

    private static final long serialVersionUID = 987709282478686584L;

    @Basic
    @NotNull
    @Size(max = 64)
    @Column(name = "CITY")
    private String city;

    @Basic
    @NotNull
    @Pattern(regexp = "\\d{2}-\\d{3}")
    @Column(name = "POSTAL_CODE")
    private String postalCode;

    @Basic
    @NotNull
    @Size(max = 128)
    @Column(name = "STREET")
    private String street;

    @Basic
    @NotNull
    @Size(max = 32)
    @Column(name = "STREET_NUMBER")
    private String streetNumber;

    @Basic
    @NotNull
    @Size(max = 32)
    @Column(name = "HOUSE_NUMBER")
    private String houseNumber;

    @Basic
    @NotNull
    @Size(max = 64)
    @Column(name = "COUNTRY")
    private String country;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "EMPLOYER_ID")
    @NotNull
    @XmlTransient
    private Employer employer;

}