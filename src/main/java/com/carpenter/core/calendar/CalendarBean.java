package com.carpenter.core.calendar;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;

public abstract class CalendarBean implements Serializable {

    private static final long serialVersionUID = 2716964108464275293L;

    private Mode mode;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    public Date getStartDateLegacy() {
        return Date.from(startDate.atZone(ZoneId.systemDefault()).toInstant());
    }

    public void setMode(Mode mode) {
        this.mode = mode;
        LocalDateTime now = LocalDateTime.now();

        switch (mode) {
            case DAY:
                startDate = now.truncatedTo(ChronoUnit.DAYS);
                endDate = startDate.plusDays(1);
                break;
            case WEEK:
                startDate = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).truncatedTo(ChronoUnit.DAYS);
                endDate = startDate.with(TemporalAdjusters.next(DayOfWeek.MONDAY)).truncatedTo(ChronoUnit.DAYS);
                break;
            case MONTH:
                startDate = now.with(TemporalAdjusters.firstDayOfMonth()).truncatedTo(ChronoUnit.DAYS);
                endDate = now.with(TemporalAdjusters.firstDayOfNextMonth()).truncatedTo(ChronoUnit.DAYS);
                break;
        }
    }

    public void moveForward() {

        switch (mode) {
            case DAY:
                startDate = startDate.plusDays(1);
                endDate = endDate.plusDays(1);
                break;

            case WEEK:
                startDate = startDate.plusWeeks(1);
                endDate = endDate.plusWeeks(1);
                break;

            case MONTH:
                startDate = startDate.plusMonths(1);
                endDate = endDate.plusMonths(1);
                break;
        }
    }

    public void moveBackward() {

        switch (mode) {
            case DAY:
                startDate = startDate.minusDays(1);
                endDate = endDate.minusDays(1);
                break;

            case WEEK:
                startDate = startDate.minusWeeks(1);
                endDate = endDate.minusWeeks(1);
                break;

            case MONTH:
                startDate = startDate.minusMonths(1);
                endDate = endDate.minusMonths(1);
                break;
        }
    }
}