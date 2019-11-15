package com.carpenter.core.model;

import com.carpenter.utils.ConstantsRegex;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.management.relation.Role;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.*;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(
        name = "EMPLOYERS",
        uniqueConstraints = {@UniqueConstraint(name = "UNIQUE_EMAIL", columnNames = {"EMAIL"})
        })
@Access(AccessType.FIELD)
@XmlRootElement(name = "user")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class Employee extends NamedEntity {

    private static final long serialVersionUID = -3270776706987062366L;

    @Column(name = "EMAIL")
    @NotNull
    @Pattern(regexp = ConstantsRegex.EMAIL_PATTERN)
    private String email;

    @NotNull
    @XmlTransient
    @Column(name = "PASSWORD")
    private String password;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "EMPLOYERS_ROLES", joinColumns = @JoinColumn(name = "EMPLOYERS_ID"))
    @Column(name = "ROLES")
    @Enumerated(EnumType.STRING)
    private Set<Roles> roles;

    @Basic
    @Size(max = 16)
    @Column(name = "NIP_NUMBER")
    private String nipNumber;

    @Basic
    @Column(name = "SELF_EMPLOYMENT")
    @XmlElement(name = "self-employment")
    private Boolean selfEmployment;

    @Basic
    @Column(name = "BANK_ACCOUNT_NUMBER")
    private String bankAccountNumber;

}
