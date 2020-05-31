package com.carpenter.core.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@MappedSuperclass
@Access(AccessType.FIELD)
public class DomainObject implements Serializable {

    private static final long serialVersionUID = 7537366168761136L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    @Past
    @XmlTransient
    @Column(name = "CREATE_DATE")
    private Date createDate;

    @Basic
    @XmlTransient
    @Column(name = "CREATE_BY")
    private String createBy;

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
