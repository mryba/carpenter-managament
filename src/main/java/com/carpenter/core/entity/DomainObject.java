package com.carpenter.core.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Past;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Getter
@Setter
@MappedSuperclass
@Access(AccessType.FIELD)
public abstract class DomainObject implements Serializable {

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DomainObject)) return false;
        DomainObject that = (DomainObject) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(createDate, that.createDate) &&
                Objects.equals(createBy, that.createBy) &&
                Objects.equals(deleteDate, that.deleteDate) &&
                Objects.equals(deletedBy, that.deletedBy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, createDate, createBy, deleteDate, deletedBy);
    }
}
