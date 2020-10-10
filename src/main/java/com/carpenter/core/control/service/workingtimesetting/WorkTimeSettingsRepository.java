package com.carpenter.core.control.service.workingtimesetting;

import com.carpenter.core.entity.WorkTimeSetting;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.NoSuchElementException;

@Stateless
public class WorkTimeSettingsRepository implements Serializable {
    private static final long serialVersionUID = -2527471278763915580L;

    @PersistenceContext
    private EntityManager entityManager;

    public WorkTimeSetting findWorkTimeSettings() {
        try {
            return entityManager.createQuery("SELECT wt FROM WorkTimeSetting wt WHERE wt.id = 1L", WorkTimeSetting.class)
                    .getResultList().iterator().next();
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    public void updateSettings(WorkTimeSetting workTimeSetting) {
        entityManager.merge(workTimeSetting);
    }
}
