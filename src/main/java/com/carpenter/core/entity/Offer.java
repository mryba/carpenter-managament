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

@EqualsAndHashCode(callSuper = true, exclude = "company")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "OFFERS")
@Entity
@Builder
@Access(AccessType.FIELD)
@NamedQueries(
        {
                @NamedQuery(
                        name = "Offer.findOfferById",
                        query = "SELECT o FROM Offer o " +
                                "WHERE o.id=:offerId"
                )
        }
)
public class Offer extends DomainObject {

    @Size(max = 64)
    @Column(name = "WORK_CITY")
    private String workCity;

    @Column(name = "WORK_DATE_FROM")
    @Temporal(TemporalType.DATE)
    private Date workDateFrom;

    @Column(name = "ARCHITECTURE_TYPE")
    @Enumerated(EnumType.STRING)
    private ArchitectureType architectureType;

    @Column(name = "FORENAME_OF_CALLING")
    private String forenameOfCalling;

    @Column(name = "COMPANY_NAME")
    private String companyName;

    @Column(name = "PHONE")
    @Size(max = 16)
    private String phone;

    @Column(name = "EMAIL")
    @NotNull
    @Pattern(regexp = ConstantsRegex.EMAIL_PATTERN)
    private String email;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "IS_READ")
    @NotNull
    private Boolean isRead;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COMPANY_ID")
    private Company company;
}
