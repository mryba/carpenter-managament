package com.carpenter.core.entity.employee;

import com.carpenter.core.entity.Company;
import com.carpenter.core.entity.dictionaries.Contract;
import com.carpenter.core.entity.dictionaries.Gender;
import com.carpenter.core.entity.dictionaries.Role;
import com.carpenter.utils.ConstantsRegex;
import com.carpenter.utils.MobilPhoneNumberAdapter;
import com.carpenter.core.entity.DomainObject;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static com.carpenter.utils.ConstantsRegex.MSISDN_PATTERN;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(
        name = "EMPLOYERS",
        uniqueConstraints = {@UniqueConstraint(name = "UNIQUE_EMAIL", columnNames = {"EMAIL"})
        })
@NamedQueries(
        @NamedQuery(
                name = "Employee.findEmployerByEmail",
                query = "SELECT e FROM Employer e " +
                        "LEFT JOIN FETCH e.addresses " +
                        "LEFT JOIN FETCH e.roles " +
                        "LEFT JOIN FETCH e.company " +
                        "WHERE e.email=:email"
        )
)
@Access(AccessType.FIELD)
public class Employer extends DomainObject {

    private static final long serialVersionUID = -3270776706987062366L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COMPANY_ID")
    private Company company;

    @Column(name = "EMAIL")
    @NotNull
    @Pattern(regexp = ConstantsRegex.EMAIL_PATTERN)
    private String email;

    @NotNull
    @XmlTransient
    @Column(name = "PASSWORD")
    private String password;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.LAZY)
    @CollectionTable(name = "EMPLOYERS_ROLES", joinColumns = @JoinColumn(name = "ID"))
    @Column(name = "ROLE")
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;

    @Basic
    @Size(max = 16)
    @Column(name = "NIP_NUMBER")
    private String nipNumber;

    @Column(name = "CONTRACT")
    @Enumerated(EnumType.STRING)
    private Contract contract;

    @Basic
    @Column(name = "BANK_ACCOUNT_NUMBER")
    private String bankAccountNumber;

    @NotNull
    @Size(max = 256)
    @Column(name = "NAME")
    private String firstName;

    @Basic
    @NotNull
    @Size(max = 256)
    @Column(name = "LAST_NAME")
    private String lastName;

    @Enumerated(EnumType.STRING)
    @Column(name = "GENDER")
    @XmlTransient
    private Gender gender;

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL}, orphanRemoval = true)
    @XmlTransient
    private List<Address> addresses;

    @Basic
    @Pattern(regexp = MSISDN_PATTERN)
    @Size(max = 16)
    @XmlJavaTypeAdapter(MobilPhoneNumberAdapter.class)
    @Column(name = "PHONE_NUMBER")
    private String phoneNumber;

    @Transient
    public boolean isInRole(Collection<Role> roles) {
        if (roles == null) {
            return false;
        }
        return !Collections.disjoint(this.roles, roles);
    }

    @Transient
    public boolean isInRole(String eRole) {
        if (roles == null) {
            return false;
        }
        Role role = Role.getRole(eRole);
        return this.roles.contains(role);
    }
}
