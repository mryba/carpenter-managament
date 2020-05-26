package com.carpenter.core.control.service.calendar;

import com.carpenter.core.control.service.login.PrincipalBean;
import lombok.Getter;
import lombok.Setter;

import javax.inject.Inject;
import java.io.Serializable;

@Getter
@Setter
public abstract class CalendarBean implements Serializable {

    private static final long serialVersionUID = 4437366168733136L;

    @Inject
    CalendarTimeManager timeManager;

    @Inject
    PrincipalBean principalBean;

    public void initCalendarMode(Mode mode) {
        timeManager.setMode(mode);
    }

    public void moveForward() {
        timeManager.moveForward();
    }

    public void moveBackward() {
        timeManager.moveBackward();
    }
}
