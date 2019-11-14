package com.carpenter.core.model;

import com.carpenter.utils.MobilPhoneNumberAdapter;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.List;

import static com.carpenter.utils.ConstantsRegex.MSISDN_PATTERN;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "NAMED_ENTITIES")
@XmlRootElement(name = "named-entity")
@XmlAccessorType(XmlAccessType.FIELD)
public class NamedEntity extends DomainObject {

    private static final long serialVersionUID = 403093192728157127L;

    @NotNull
    @Size(max = 256)
    @Column(name = "NAME")
    private String name;

    @Basic
    @NotNull
    @Size(max = 256)
    @Column(name = "LAST_NAME")
    private String lastName;

    @Enumerated(EnumType.STRING)
    @Column(name = "GENDER")
    @XmlTransient
    private Gender gender;

    @Basic
    @NotNull
    @Size(max = 256)
    @OneToMany(
            mappedBy = "namedEntity", fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH}, orphanRemoval = true)
    @OrderColumn(name = "ADDRESSES_ORDER")
    @Valid
    @XmlTransient
    private List<Address> addresses;

    @Basic
    @Pattern(regexp = MSISDN_PATTERN)
    @Size(max = 16)
    @XmlJavaTypeAdapter(MobilPhoneNumberAdapter.class)
    private String phoneNumber;
}