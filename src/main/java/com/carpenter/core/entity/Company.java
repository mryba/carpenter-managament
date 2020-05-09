package com.carpenter.core.entity;

import com.carpenter.core.entity.employee.Employer;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "COMPANY")
@Entity
@Access(AccessType.FIELD)
public class Company extends DomainObject {

    private static final long serialVersionUID = -5294397336583896621L;

    @Column(name = "NAME")
    private String name;

    @Column(name = "CITY")
    private String city;

    @Column(name = "POSTAL_CODE")
    private String postalCode;

    @Column(name = "STREET")
    private String street;

    @Column(name = "COUNTRY")
    private String country;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "PHONE")
    private String phone;

    @OneToMany(mappedBy = "company")
    private Set<Employer> employers;

    public void addEmployee(Employer employer) {
        if (this.employers == null) {
            employers = new LinkedHashSet<>();
        }
        this.employers.add(employer);
    }
}
