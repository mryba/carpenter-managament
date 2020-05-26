package com.carpenter.core.control.service.calendar;

import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

@Named("calendarWeekBean")
@ViewScoped
@Slf4j
public class CalendarWeekBean extends CalendarBean {

    private static final long serialVersionUID = 7818636965206530503L;

    @PostConstruct
    public void initWeekCalendar() {
        initCalendarMode(Mode.WEEK);
    }

}
