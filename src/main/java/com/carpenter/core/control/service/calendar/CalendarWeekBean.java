package com.carpenter.core.control.service.calendar;

import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Named("calendarWeekBean")
@ViewScoped
@Slf4j
public class CalendarWeekBean extends CalendarBean {

    private static final long serialVersionUID = 7818636965206530503L;

    @PostConstruct
    public void initWeekCalendar() {
        initCalendarMode(Mode.WEEK);
    }

    public List<Date> getDates() {
        List<Date> dates = new LinkedList<>();
        LocalDateTime startDate = timeManager.getViewStartDate();
        while (startDate.isBefore(timeManager.getViewEndDate())) {
            dates.add(Date.from(startDate.atZone(ZoneId.systemDefault()).toInstant()));
            startDate = startDate.plusDays(1);
        }
        return dates;
    }
}
