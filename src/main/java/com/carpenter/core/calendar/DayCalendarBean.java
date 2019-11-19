package com.carpenter.core.calendar;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

@Named
@ViewScoped
public class DayCalendarBean extends CalendarBean {

    private static final long serialVersionUID = 7343634764634087609L;

    @PostConstruct
    public void init(){
        setMode(Mode.DAY);
    }



}
