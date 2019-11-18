package com.carpenter.utils.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

@Data
@MappedSuperclass
@Access(AccessType.FIELD)
public class DomainObject implements Serializable {

    private static final long serialVersionUID = 7537366168761136L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    @NotNull
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    @Past
    @XmlTransient
    @Column(name = "CREATE_DATE")
    private Date createDate;

    @Basic
    @XmlTransient
    @Column(name = "CREATE_BY")
    private Date createBy;

    @Temporal(TemporalType.TIMESTAMP)
    @XmlTransient
    @Past
    @Column(name = "DELETE_DATE")
    private Date deleteDate;

    @Basic
    @XmlTransient
    @Column(name = "DELETED_BY")
    private String deletedBy;
}
