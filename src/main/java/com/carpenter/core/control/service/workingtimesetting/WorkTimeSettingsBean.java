package com.carpenter.core.control.service.workingtimesetting;

import com.carpenter.core.entity.WorkTimeSetting;

import javax.annotation.PostConstruct;
import javax.ejb.Local;
import javax.enterprise.context.SessionScoped;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@SessionScoped
@Named("workingTimeSettingsBean")
public class WorkTimeSettingsBean implements Serializable {

    private static final long serialVersionUID = -8049715074307174540L;

    private WorkTimeSetting workTimeSetting;

    @PostConstruct
    private void init() {
        this.workTimeSetting = workTimeSettingsService.initWorkTime();
    }

    @Inject
    WorkTimeSettingsService workTimeSettingsService;

    public boolean isSchedulerActive() {
        return workTimeSetting.getMechanismActive();
    }

    public Integer getStaticHours() {
        return workTimeSetting.getStaticHour();
    }

    public void setStaticHours(Integer staticHours) {
        workTimeSetting.setStaticHour(staticHours);
        workTimeSettingsService.updateSettings(workTimeSetting);
    }

    public void toggle() {
        Boolean mechanismActive = this.workTimeSetting.getMechanismActive();
        if (Boolean.TRUE.equals(mechanismActive)) {
            this.workTimeSetting.setMechanismActive(Boolean.FALSE);
        } else {
            this.workTimeSetting.setMechanismActive(Boolean.TRUE);
        }
        workTimeSettingsService.updateSettings(workTimeSetting);
    }
}
