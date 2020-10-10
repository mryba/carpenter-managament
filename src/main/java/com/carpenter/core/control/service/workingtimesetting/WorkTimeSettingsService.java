package com.carpenter.core.control.service.workingtimesetting;

import com.carpenter.core.entity.WorkTimeSetting;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.io.Serializable;

@Stateless
public class WorkTimeSettingsService implements Serializable {

    private static final long serialVersionUID = -2244680612041064058L;

    @Inject
    private WorkTimeSettingsRepository workingTimeRepository;

    public WorkTimeSetting initWorkTime() {
       return workingTimeRepository.findWorkTimeSettings();
    }

    public void updateSettings(WorkTimeSetting workTimeSetting) {
        workingTimeRepository.updateSettings(workTimeSetting);

    }
}
