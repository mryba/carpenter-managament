package com.carpenter.core.control.service.audit;

import com.carpenter.core.control.dto.AuditTrailDto;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Named("auditTrailBean")
@ViewScoped
public class AuditTrailBean implements Serializable {
    private static final long serialVersionUID = 2349857198349128L;

    @Inject
    AuditTrailService auditTrailService;

    @Getter
    @Setter
    private AuditTrailDto auditTrailDto;
    private AuditTrailMapper auditTrailMapper;

    @PostConstruct
    public void init() {
        auditTrailDto = new AuditTrailDto();
    }

    public List<AuditTrailDto> getAuditTrailList() {
        auditTrailMapper = new AuditTrailMapper();
        return auditTrailService.getAuditTrailsList()
                .stream()
                .map(auditTrail -> auditTrailMapper.mapToDomain(auditTrail))
                .collect(Collectors.toList());
    }

    public void addAuditTrail(AuditTrailDto auditTrailDto) {
        auditTrailMapper = new AuditTrailMapper();
        auditTrailService.addAuditTrail(auditTrailMapper.mapFromDomain(auditTrailDto));
    }
}
