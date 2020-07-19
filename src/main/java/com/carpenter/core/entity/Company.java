package com.carpenter.core.entity;

import com.carpenter.core.entity.client.Client;
import com.carpenter.core.entity.employee.Employee;
import lombok.*;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
@Table(name = "COMPANY")
@Entity
@Access(AccessType.FIELD)
@NamedQueries({
        @NamedQuery(
                name = "Company.findAllActiveCompanies",
                query = "SELECT new com.carpenter.core.control.dto.CompanyDto(c.id, c.name) FROM Company c WHERE c.deleteDate is NULL"
        ),
        @NamedQuery(
                name = "Company.findById",
                query = "SELECT distinct c FROM Company c " +
                        "LEFT JOIN FETCH c.offers " +
                        "WHERE c.id =:companyId"
        ),
        @NamedQuery(
                name = "Company.findByName",
                query = "SELECT c FROM Company c LEFT JOIN FETCH c.offers WHERE c.name =:companyName"
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
    private List<Employee> employees;

    @OneToMany(mappedBy = "company", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Client> clients;

    @OneToMany(mappedBy = "company", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Offer> offers;

    public void addOffer(Offer offer) {
        if (this.offers == null) {
            this.offers = new LinkedList<>();
        }
        this.offers.add(offer);
        offer.setCompany(this);
    }

    public void addClient(Client client) {
        if (this.clients == null) {
            this.clients = new LinkedList<>();
        }
        this.clients.add(client);
        client.setCompany(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Company)) return false;
        if (!super.equals(o)) return false;
        Company company = (Company) o;
        return Objects.equals(name, company.name) &&
                Objects.equals(city, company.city) &&
                Objects.equals(postalCode, company.postalCode) &&
                Objects.equals(street, company.street) &&
                Objects.equals(country, company.country) &&
                Objects.equals(email, company.email) &&
                Objects.equals(phone, company.phone) &&
                Objects.equals(employees, company.employees) &&
                Objects.equals(clients, company.clients) &&
                Objects.equals(offers, company.offers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name, city, postalCode, street, country, email, phone, employees, clients, offers);
    }
}
