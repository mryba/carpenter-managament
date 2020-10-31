package com.carpenter.core.control.service.audit;

import com.carpenter.core.entity.employee.Employee;
import lombok.Getter;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

@Getter
@ApplicationScoped
@Named("auditLoggedUsers")
public class AuditLoggedUsers implements Serializable {

    private static final long serialVersionUID = -1591747123423557299L;

    private final List<Employee> loggedUsers = new LinkedList<>();

    public void addLoggedUserToList(Employee employee) {
        loggedUsers.add(employee);
    }

    public void remove(Employee loggedUser) {
        loggedUsers.stream().filter(e -> e.getId().equals(loggedUser.getId())).findFirst().ifPresent(loggedUsers::remove);
    }
}
