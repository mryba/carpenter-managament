package com.carpenter.core.control.service.audit;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Event;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.Collection;
import java.util.Date;

@Named("auditTrailFilters")
@ViewScoped
public class AuditTrailFilters implements Serializable {

    private static final long serialVersionUID = 8238725967446767529L;

    @Inject
    private Event<AuditTrailFilters> event;

    private Collection<Long> employeeFilter;
    private LocalDateTime dateFromFilter;
    private LocalDateTime dateToFilter;

    @PostConstruct
    public void init() {
        LocalDateTime now = LocalDateTime.now();
        dateFromFilter = now.with(TemporalAdjusters.firstDayOfMonth()).truncatedTo(ChronoUnit.DAYS);
        dateToFilter = now.with(TemporalAdjusters.lastDayOfMonth()).truncatedTo(ChronoUnit.DAYS).toLocalDate().atTime(LocalTime.MAX);
    }

    public Collection<Long> getEmployeeFilter() {
        return employeeFilter;
    }

    public void setEmployeeFilter(Collection<Long> employeeFilter) {
        this.employeeFilter = employeeFilter;
        event.fire(this);
    }

    public Date getDateFromFilter() {
        if (dateFromFilter != null) {
            return Date.from(dateFromFilter.atZone(ZoneId.systemDefault()).toInstant());
        }
        return null;
    }

    public void setDateFromFilter(Date dateFromFilter) {
        this.dateFromFilter = dateFromFilter.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        event.fire(this);
    }

    public Date getDateToFilter() {
        if (dateToFilter != null) {
            return Date.from(dateToFilter.atZone(ZoneId.systemDefault()).toInstant());
        }
        return null;
    }

    public void setDateToFilter(Date dateToFilter) {
        this.dateToFilter = dateToFilter.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().atTime(LocalTime.MAX);
        event.fire(this);
    }
}
