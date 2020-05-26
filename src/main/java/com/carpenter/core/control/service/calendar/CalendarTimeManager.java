package com.carpenter.core.control.service.calendar;

import lombok.Getter;
import lombok.Setter;

import javax.enterprise.context.Dependent;
import javax.inject.Named;
import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;

@Getter
@Setter
@Named("calendarTimeManager")
@Dependent
public class CalendarTimeManager implements Serializable {

    private static final long serialVersionUID = -3365593739466001798L;

    private LocalDateTime viewStartDate = null;
    private LocalDateTime viewEndDate = null;
    private Mode mode;

    public void setMode(Mode mode) {
        this.mode = mode;
        if (mode == Mode.DAY) {
            viewStartDate = LocalDateTime.now().truncatedTo(ChronoUnit.DAYS);
            viewEndDate = viewStartDate.plusDays(1);
        }
        if (mode == Mode.WEEK) {
            viewStartDate = LocalDateTime.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).truncatedTo(ChronoUnit.DAYS);
            viewEndDate = viewStartDate.with(TemporalAdjusters.next(DayOfWeek.MONDAY)).truncatedTo(ChronoUnit.DAYS);
        }
        if (mode == Mode.MONTH) {
            viewStartDate = LocalDateTime.now().with(TemporalAdjusters.firstDayOfMonth()).truncatedTo(ChronoUnit.DAYS);
            viewEndDate = LocalDateTime.now().with(TemporalAdjusters.lastDayOfMonth()).truncatedTo(ChronoUnit.DAYS);
        }
    }

    public void moveForward(){
        if (mode == Mode.DAY) {
            viewStartDate = viewStartDate.plusDays(1);
            viewEndDate = viewEndDate.plusDays(1);
        }
        if (mode == Mode.WEEK) {
            viewStartDate = viewStartDate.plusWeeks(1);
            viewEndDate = viewEndDate.plusWeeks(1);
        }
        if (mode == Mode.MONTH) {
            viewStartDate = viewStartDate.plusMonths(1);
            viewEndDate = viewEndDate.plusMonths(1);
        }
    }

    public void moveBackward(){
        if (mode == Mode.DAY) {
            viewStartDate = viewStartDate.minusDays(1);
            viewEndDate = viewEndDate.minusDays(1);
        }
        if (mode == Mode.WEEK) {
            viewStartDate = viewStartDate.minusWeeks(1);
            viewEndDate = viewEndDate.minusWeeks(1);
        }
        if (mode == Mode.MONTH) {
            viewStartDate = viewStartDate.minusMonths(1);
            viewEndDate = viewEndDate.minusMonths(1);
        }
    }

    public Date getStartDate(){
        return Date.from(viewStartDate.atZone(ZoneId.systemDefault()).toInstant());
    }

    public Date getEndDate(){
        return Date.from(viewEndDate.atZone(ZoneId.systemDefault()).toInstant());
    }

}
