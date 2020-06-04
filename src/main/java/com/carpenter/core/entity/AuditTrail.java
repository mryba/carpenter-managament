package com.carpenter.core.entity;

import com.carpenter.core.entity.dictionaries.Activity;
import com.carpenter.core.entity.employee.Employee;
import lombok.*;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Builder
@Table(name = "AUDIT_TRAILS")
@NamedQuery(name = "AuditTrail.findAll", query = "SELECT a FROM AuditTrail a")
@Access(AccessType.FIELD)
public class AuditTrail extends DomainObject {
    private static final long serialVersionUID = 3501239749381341L;

    @Enumerated(EnumType.STRING)
    @Column(name = "ACTIVITY")
    private Activity activity;

    @ManyToOne
    @JoinColumn(name = "EMPLOYEE_ID")
    private Employee employee;
}
