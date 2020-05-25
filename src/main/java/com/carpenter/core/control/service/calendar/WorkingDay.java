package com.carpenter.core.control.service.calendar;

import com.carpenter.core.entity.DomainObject;
import com.carpenter.core.entity.client.Client;
import com.carpenter.core.entity.employee.Employee;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "WORKING_DAYS")
@Access(AccessType.FIELD)
public class WorkingDay extends DomainObject {

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "EMPLOYEES_DAYS",
            joinColumns = {
                    @JoinColumn(name = "WORKING_DAY_ID")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "EMPLOYEE_ID")
            }
    )
    private Set<Employee> employees;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CLIENT_ID", referencedColumnName = "ID")
    private Client client;

    @Column(name = "HOURS")
    private Integer hours;

    public void addWorkingDay(Employee employee) {
        employees.add(employee);
        employee.getWorkingDay().add(this);
    }

    public void removeWorkingDay(Employee employee) {
        employees.remove(employee);
        employee.getWorkingDay().remove(this);
    }
}
