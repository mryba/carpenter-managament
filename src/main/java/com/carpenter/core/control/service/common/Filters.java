package com.carpenter.core.control.service.common;


import javax.annotation.PostConstruct;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;

public class Filters implements Serializable {

    private static final long serialVersionUID = 5763621645710822240L;

    private LocalDateTime dateFromFilter;
    private LocalDateTime dateToFilter;

    @Inject
    private Event<Filters> event;

    @PostConstruct
    public void init() {
        LocalDateTime now = LocalDateTime.now();
        dateFromFilter = now.with(TemporalAdjusters.firstDayOfMonth()).truncatedTo(ChronoUnit.DAYS);
        dateToFilter = now.with(TemporalAdjusters.lastDayOfMonth()).truncatedTo(ChronoUnit.DAYS).toLocalDate().atTime(LocalTime.MAX);
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
