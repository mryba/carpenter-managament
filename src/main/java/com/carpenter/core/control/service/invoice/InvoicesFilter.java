package com.carpenter.core.control.service.invoice;

import com.carpenter.core.control.service.common.Filters;

import javax.enterprise.event.Event;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Collection;

@ViewScoped
@Named("invoicesFilter")
public class InvoicesFilter extends Filters implements Serializable {

    private static final long serialVersionUID = 2744206648881110854L;

    private Collection<Long> employeeIds;
    private Collection<Long>clientIds;

    @Inject
    private Event<InvoicesFilter> event;

    public Collection<Long> getEmployeeIds() {
        return employeeIds;
    }

    public void setEmployeeIds(Collection<Long> employeeIds) {
        this.employeeIds = employeeIds;
        event.fire(this);
    }

    public Collection<Long> getClientIds() {
        return clientIds;
    }

    public void setClientIds(Collection<Long> clientIds) {
        this.clientIds = clientIds;
        event.fire(this);
    }
}
