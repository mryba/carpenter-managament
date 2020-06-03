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
@Table(name = "AUDITRAILS")
@Access(AccessType.FIELD)
public class AudiTrails extends DomainObject {
    private static final long serialVersionUID = 3501239749381341L;

    @Enumerated(EnumType.STRING)
    @Column(name = "ACTIVITY")
    private Activity activity;

    @ManyToOne
    @JoinColumn(name = "EMPLOYEE_ID")
    private Employee employee;
}
