package com.carpenter.core.control.service.audit;

import javax.enterprise.event.Event;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Collection;

@Named("auditTrailFilters")
@ViewScoped
public class AuditTrailFilters implements Serializable {

    private static final long serialVersionUID = 8238725967446767529L;

    @Inject
    private Event<AuditTrailFilters> event;

    private Collection<Long> employeeFilter;

    public Collection<Long> getEmployeeFilter() {
        return employeeFilter;
    }

    public void setEmployeeFilter(Collection<Long> employeeFilter) {
        this.employeeFilter = employeeFilter;
        event.fire(this);
    }
}
