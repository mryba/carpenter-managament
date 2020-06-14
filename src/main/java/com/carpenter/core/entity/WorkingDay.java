package com.carpenter.core.entity;

import com.carpenter.core.entity.client.Client;
import com.carpenter.core.entity.employee.Employee;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "WORKING_DAYS")
@Access(AccessType.FIELD)
@NamedQueries({
        @NamedQuery(
                name = "WorkingDay.findEmployeeWorkDay",
                query = "SELECT w FROM WorkingDay w WHERE w.employee.id =:employeeId AND w.day = :day"
        )
})
public class WorkingDay extends DomainObject {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EMPLOYEE_ID")
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CLIENT_ID", referencedColumnName = "ID")
    private Client client;

    @Column(name = "HOURS")
    private Integer hours;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DAY")
    private Date day;

    @Column(name = "EDIT_DATE")
    private Date editDate;
}
