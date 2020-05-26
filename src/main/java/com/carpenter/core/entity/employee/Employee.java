package com.carpenter.core.entity.employee;

import com.carpenter.core.entity.WorkingDay;
import com.carpenter.core.entity.Company;
import com.carpenter.core.entity.DomainObject;
import com.carpenter.core.entity.dictionaries.Contract;
import com.carpenter.core.entity.dictionaries.Gender;
import com.carpenter.core.entity.dictionaries.Role;
import com.carpenter.utils.ConstantsRegex;
import com.carpenter.utils.MobilPhoneNumberAdapter;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.*;

import static com.carpenter.utils.ConstantsRegex.MSISDN_PATTERN;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(
        name = "EMPLOYEES",
        uniqueConstraints = {@UniqueConstraint(name = "UNIQUE_EMAIL", columnNames = {"EMAIL"})
        })
@NamedQueries(
        {
                @NamedQuery(
                        name = "Employee.findEmployerByEmail",
                        query = "SELECT e FROM Employee e " +
                                "LEFT JOIN FETCH e.addresses " +
                                "LEFT JOIN FETCH e.roles " +
                                "LEFT JOIN FETCH e.company " +
                                "WHERE e.email=:email"
                ),
                @NamedQuery(
                        name = "Employee.findEmployeeById",
                        query = "SELECT e FROM Employee e " +
                                "WHERE e.id =:employeeId"
                )
        }
)
@NamedEntityGraphs({
        @NamedEntityGraph(
                name = "Employee.addresses",
                attributeNodes = {
                        @NamedAttributeNode(value = "addresses")
                }
        )
})
@Access(AccessType.FIELD)
public class Employee extends DomainObject {

    private static final long serialVersionUID = -3270776706987062366L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COMPANY_ID")
    private Company company;

    @Column(name = "EMAIL")
    @NotNull
    @Pattern(regexp = ConstantsRegex.EMAIL_PATTERN)
    private String email;

    @XmlTransient
    @Column(name = "PASSWORD")
    private String password;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.LAZY)
    @CollectionTable(name = "EMPLOYEE_ROLES", joinColumns = @JoinColumn(name = "ID"))
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
    @Column(name = "FIRST_NAME")
    private String firstName;

    @Basic
    @NotNull
    @Size(max = 256)
    @Column(name = "LAST_NAME")
    private String lastName;

    @Column(name = "BIRTH_DATE")
    private Date birthDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "GENDER")
    @XmlTransient
    private Gender gender;

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL}, orphanRemoval = true)
    @JoinColumn(name = "EMPLOYEE_ID", referencedColumnName = "ID")
    @XmlTransient
    private List<Address> addresses;

    @Basic
    @Column(name = "PHONE_NUMBER")
    @Pattern(regexp = MSISDN_PATTERN)
    @Size(max = 16)
    @XmlJavaTypeAdapter(MobilPhoneNumberAdapter.class)
    private String phoneNumber;

    @Column(name = "ACCOUNT_ACTIVE")
    private Boolean accountActive;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "employee")
    private Set<WorkingDay> workingDays;

    public void addRole(Role role) {
        if (role == null) {
            return;
        }
        if (this.roles == null) {
            this.roles = new LinkedHashSet<>();
        }
        this.roles.add(role);
    }

    public void addAddress(Address address) {
        if (address == null) {
            return;
        }
        if (this.addresses == null) {
            this.addresses = new LinkedList<>();
        }
        this.addresses.add(address);
    }

    public void addWorkingDay(WorkingDay workingDay) {
        if (workingDays == null) {
            workingDays = new LinkedHashSet<>();
        }
        workingDays.add(workingDay);
        workingDay.setEmployee(this);
    }

    public void removeWorkingDay(WorkingDay workingDay) {
        if (workingDays != null) {
            workingDays.remove(workingDay);
            workingDay.setEmployee(null);
        }
    }

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
