package com.carpenter.core.control.service.audit;

import com.carpenter.core.control.dto.AuditTrailDto;
import com.carpenter.core.entity.AuditTrail;
import com.carpenter.core.entity.dictionaries.Activity;
import com.carpenter.utils.Mapper;

public class AuditTrailMapper implements Mapper<AuditTrail, AuditTrailDto> {
    @Override
    public AuditTrail mapFromDomain(AuditTrailDto auditTrailDto) {
        return AuditTrail.builder()
                .activity(Activity.valueOf((auditTrailDto.getActivity())))
                .employee(auditTrailDto.getEmployee())
                .build();
    }

    @Override
    public AuditTrailDto mapToDomain(AuditTrail auditTrail) {
        return AuditTrailDto.builder()
                .activity(auditTrail.getActivity().name())
                .employee(auditTrail.getEmployee())
                .build();
    }
}
