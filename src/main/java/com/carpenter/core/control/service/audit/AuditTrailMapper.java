package com.carpenter.core.control.service.audit;

import com.carpenter.core.control.dto.AuditTrailDto;
import com.carpenter.core.entity.AuditTrail;
import com.carpenter.utils.Mapper;

public class AuditTrailMapper implements Mapper<AuditTrail, AuditTrailDto> {
    @Override
    public AuditTrail mapFromDomain(AuditTrailDto auditTrailDto) {
        return null;
    }

    @Override
    public AuditTrailDto mapToDomain(AuditTrail auditTrail) {
        return AuditTrailDto.builder()
                .activity(auditTrail.getActivity().name())
                .employeeId(auditTrail.getEmployee().getFirstName())
                .build();
    }
}
