package com.carpenter.core.control.service.workingday;

import javax.enterprise.event.Event;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

@ViewScoped
@Named("workTimeListener")
public class WorkTimeListener implements Serializable {

    private static final long serialVersionUID = -3820865122432992709L;

    private Long employeeGroupId;

    @Inject
    private Event<WorkTimeListener> event;

    public Long getEmployeeGroupId() {
        return employeeGroupId;
    }

    public void setEmployeeGroupId(Long employeeGroupId) {
        this.employeeGroupId = employeeGroupId;
        event.fire(this);
    }
}
