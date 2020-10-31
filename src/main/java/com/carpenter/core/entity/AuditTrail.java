package com.carpenter.core.entity;

import com.carpenter.core.entity.employee.Employee;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "AUDIT_TRIALS")
@Access(AccessType.FIELD)
public class AuditTrail extends DomainObject {

    private static final long serialVersionUID = 6015113092001426396L;

    @ManyToOne
    @JoinColumn(name = "EMPLOYEE_ID")
    private Employee employee;

    @Column(name = "CLIENT_IP")
    private String clientIp;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AuditTrail)) return false;
        if (!super.equals(o)) return false;
        AuditTrail that = (AuditTrail) o;
        return Objects.equals(employee, that.employee) &&
                Objects.equals(clientIp, that.clientIp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), employee, clientIp);
    }
}
