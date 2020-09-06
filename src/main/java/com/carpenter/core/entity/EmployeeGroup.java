package com.carpenter.core.entity;

import com.carpenter.core.entity.client.Client;
import com.carpenter.core.entity.employee.Employee;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "EMPLOYEE_GROUPS")
@Access(AccessType.FIELD)
public class EmployeeGroup extends DomainObject {

    private static final long serialVersionUID = -4220277435589526222L;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "employeeGroup", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Employee> employees;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PRESENT_CLIENT", referencedColumnName = "ID")
    private Client presentClient;

    @Column(name = "NAME")
    private String groupName;

    public void addEmployee(Employee employee) {
        if (employees == null) {
            employees = new ArrayList<>();
        }
        employees.add(employee);
        employee.setEmployeeGroup(this);
    }

    public void removeEmployee(Employee employee) {
        if (employees != null) {
            employee.setEmployeeGroup(null);
            employees.remove(employee);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EmployeeGroup)) return false;
        if (!super.equals(o)) return false;
        EmployeeGroup that = (EmployeeGroup) o;
        return Objects.equals(employees, that.employees);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), employees);
    }
}
