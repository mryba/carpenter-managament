package com.carpenter.core.entity.client;

import com.carpenter.core.entity.WorkingDay;
import com.carpenter.core.entity.Company;
import com.carpenter.core.entity.DomainObject;
import lombok.*;

import javax.persistence.*;
import javax.resource.spi.work.Work;
import javax.validation.constraints.NotNull;
import java.util.LinkedHashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Builder
@Table(
        name = "CLIENTS",
        uniqueConstraints = @UniqueConstraint(name = "UNIQUE_NIP", columnNames = {"NIP"})
)
@NamedQueries({
        @NamedQuery(name = "Client.findByNip",
                query = "SELECT c FROM Client c " +
                        "WHERE c.nip =:nip"),
        @NamedQuery(
                name = "Client.findAll",
                query = "SELECT c FROM Client c"),
        @NamedQuery(
                name = "Client.findById",
                query = "SELECT c FROM Client c JOIN FETCH c.workingDays WHERE c.id =:id"
        )
})
@NamedEntityGraphs({
        @NamedEntityGraph(
                name = "Client.workingDays",
                attributeNodes = @NamedAttributeNode("workingDays")
        )
})
@Access(AccessType.FIELD)
public class Client extends DomainObject {

    @NotNull
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

    @Column(name = "BANK_ACCOUNT_NUMBER")
    private String bankAccountNumber;

    @NotNull
    @Column(name = "NIP")
    private String nip;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "PHONE_NUMBER")
    private String phoneNumber;

    @Column(name = "WEB_SITE")
    private String webSite;

    @Column(name = "STREET_NUMBER")
    private String streetNumber;

    @Column(name = "HOUSE_NUMBER")
    private String houseNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COMPANY_ID", referencedColumnName = "ID")
    private Company company;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "client")
    private Set<WorkingDay> workingDays;

    public void addWorkingDat(WorkingDay workingDay) {
        if (workingDays == null) {
            workingDays = new LinkedHashSet<>();
        }
        workingDays.add(workingDay);
        workingDay.setClient(this);
    }

}
