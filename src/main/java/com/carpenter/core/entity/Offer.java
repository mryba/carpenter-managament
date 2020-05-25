package com.carpenter.core.entity;

import com.carpenter.core.entity.dictionaries.ArchitectureType;
import com.carpenter.utils.ConstantsRegex;
import com.carpenter.utils.MobilPhoneNumberAdapter;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "OFFERS")
@Entity
@Builder
@Access(AccessType.FIELD)
public class Offer extends DomainObject {

    @Size(max = 64)
    @Column(name = "WORK_CITY")
    private String workCity;

    @Column(name = "WORK_DATE_FROM")
    private Date workDateFrom;

    @Column(name = "WORK_DATE_TO")
    private Date workDateTo;

    @Column(name = "BUILDING_DIMENSION")
    private Double buildingDimension;

    @Column(name = "ARCHITECTURE_TYPE")
    @Enumerated(EnumType.STRING)
    private ArchitectureType architectureType;

    @Column(name = "PHONE")
    @Pattern(regexp = ConstantsRegex.MSISDN_PATTERN)
    @Size(max = 16)
    @XmlJavaTypeAdapter(MobilPhoneNumberAdapter.class)
    private String phone;

    @Column(name = "EMAIL")
    @NotNull
    @Pattern(regexp = ConstantsRegex.EMAIL_PATTERN)
    private String email;

    @Column(name = "IS_READ")
    @NotNull
    private Boolean isRead;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COMPANY_ID")
    @NotNull
    private Company company;
}
