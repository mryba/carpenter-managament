package com.carpenter.core.entity.employee;

import com.carpenter.core.entity.DomainObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Address)) return false;
        Address address = (Address) o;
        return Objects.equals(city, address.city) &&
                Objects.equals(postalCode, address.postalCode) &&
                Objects.equals(street, address.street) &&
                Objects.equals(streetNumber, address.streetNumber) &&
                Objects.equals(houseNumber, address.houseNumber) &&
                Objects.equals(country, address.country);
    }

    @Override
    public int hashCode() {
        return Objects.hash(city, postalCode, street, streetNumber, houseNumber, country);
    }
}