package com.carpenter.core.control.service.audit;

import com.carpenter.core.control.repository.AuditTrailRepository;
import com.carpenter.core.entity.AuditTrail;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.List;

@SessionScoped
public class AuditTrailService implements Serializable {
    public static final long serialVersionUID = 5342205694651987L;

    @Inject
    AuditTrailRepository auditTrailRepository;

    public List<AuditTrail> getAudiTrailsList() {
        return auditTrailRepository.getAllAudiTrails();
    }
}
