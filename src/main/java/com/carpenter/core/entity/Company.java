package com.carpenter.core.entity;

import com.carpenter.core.entity.client.Client;
import com.carpenter.core.entity.employee.Employee;
import lombok.*;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, exclude = "employees")
@Getter
@Setter
@Table(name = "COMPANY")
@Entity
@Access(AccessType.FIELD)
@NamedQueries({
        @NamedQuery(
                name = "Company.findAllActiveCompanies",
                query = "SELECT new com.carpenter.core.control.dto.CompanyDto(c.id, c.name) FROM Company c WHERE c.deleteDate is NULL"
        )
})
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
    private Set<Employee> employees;

    @OneToMany(mappedBy = "company")
    private Set<Client> clients;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
    private List<Offer> offers;

    public void addEmployee(Employee employee) {
        if (this.employees == null) {
            employees = new LinkedHashSet<>();
        }
        this.employees.add(employee);
    }
}
