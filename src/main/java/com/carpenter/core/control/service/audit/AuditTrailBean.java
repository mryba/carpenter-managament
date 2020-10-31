package com.carpenter.core.control.service.audit;

import com.carpenter.core.control.dto.EmployeeDto;
import com.carpenter.core.control.service.employee.EmployeeService;
import com.carpenter.core.control.service.login.PrincipalBean;
import com.carpenter.core.entity.AuditTrail;
import com.carpenter.core.entity.employee.Employee;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Observes;
import javax.enterprise.event.Reception;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Getter
@Setter
@ViewScoped
@Named("audiTrailBean")
public class AuditTrailBean implements Serializable {

    private static final long serialVersionUID = -6518823109555890992L;

    @Inject
    private AuditTrailService auditTrailService;

    @Inject
    private PrincipalBean principalBean;

    @Inject
    private EmployeeService employeeService;

    @Inject
    private AuditTrailFilters filters;

    private List<AuditTrail> trailList = new LinkedList<>();
    private List<Employee> employees;

    @PostConstruct
    public void init() {
        employees = employeeService.getEmployersList(principalBean);
        refresh(filters);
    }

    public void refresh(@Observes(notifyObserver = Reception.IF_EXISTS) AuditTrailFilters filters) {
        this.filters = filters;
        trailList.clear();
        trailList = auditTrailService.getAllAuditsByFilter(filters);
    }

}
