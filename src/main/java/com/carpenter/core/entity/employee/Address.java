package com.carpenter.core.entity.employee;

import com.carpenter.core.entity.DomainObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlTransient;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "ADDRESSES")
@Access(AccessType.FIELD)
public class Address extends DomainObject {

    private static final long serialVersionUID = 987709282478686584L;

    @Basic
    @Size(max = 64)
    @Column(name = "CITY")
    private String city;

    @Basic
    @Pattern(regexp = "\\d{2}-\\d{3}", message = "Niepoprawny kod pocztowy")
    @Column(name = "POSTAL_CODE")
    private String postalCode;

    @Basic
    @Size(max = 128)
    @Column(name = "STREET")
    private String street;

    @Basic
    @Size(max = 32)
    @Column(name = "STREET_NUMBER")
    private String streetNumber;

    @Basic
    @Size(max = 32)
    @Column(name = "HOUSE_NUMBER")
    private String houseNumber;

    @Basic
    @Size(max = 64)
    @Column(name = "COUNTRY")
    private String country;

}