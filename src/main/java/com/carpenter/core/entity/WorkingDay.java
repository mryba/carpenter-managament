package com.carpenter.core.entity;

import com.carpenter.core.entity.client.Client;
import com.carpenter.core.entity.employee.Employee;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.Past;
import java.util.Date;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "WORKING_DAYS")
@Access(AccessType.FIELD)
@NamedQueries({
        @NamedQuery(
                name = "WorkingDay.findEmployeeWorkDay",
                query = "SELECT w FROM WorkingDay w WHERE w.employee.id =:employeeId AND w.day = :day"
        ),
        @NamedQuery(
                name = "WorkingDay.findAllWorkingDayByDate",
                query = "SELECT w FROM WorkingDay w " +
                        "WHERE w.day =:yesterday"
        )
})
public class WorkingDay extends DomainObject {

    private static final long serialVersionUID = 745535782216732366L;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WorkingDay)) return false;
        WorkingDay that = (WorkingDay) o;
        return Objects.equals(employee, that.employee) &&
                Objects.equals(client, that.client) &&
                Objects.equals(hours, that.hours) &&
                Objects.equals(day, that.day) &&
                Objects.equals(editDate, that.editDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(employee, client, hours, day, editDate);
    }
}
