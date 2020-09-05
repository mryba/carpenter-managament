package com.carpenter.core.entity;

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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "employeeGroup" ,cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE, CascadeType.REFRESH})
    private List<Employee> employees;

    @Column(name = "NAME")
    private String groupName;

    @Column(name = "ACTIVE")
    private Boolean active;

    public void addEmployee(Employee employee) {
        if (employees == null) {
            employees = new ArrayList<>();
        }
        employees.add(employee);
        employee.setEmployeeGroup(this);
    }

    public void remoteEmployee(Employee employee) {
        if (employees != null) {
            employees.remove(employee);
            employee.setEmployeeGroup(null);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EmployeeGroup)) return false;
        if (!super.equals(o)) return false;
        EmployeeGroup that = (EmployeeGroup) o;
        return Objects.equals(employees, that.employees) &&
                Objects.equals(active, that.active);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), employees, active);
    }
}
