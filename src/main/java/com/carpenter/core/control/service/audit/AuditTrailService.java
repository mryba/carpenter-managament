package com.carpenter.core.control.service.audit;

import com.carpenter.core.control.repository.AuditTrailRepository;
import com.carpenter.core.entity.AuditTrail;
import com.carpenter.core.entity.employee.Employee;

import javax.ejb.Stateless;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Stateless
public class AuditTrailService implements Serializable {

    private static final long serialVersionUID = -1587921324169720718L;

    @Inject
    private AuditTrailRepository auditTrailRepository;

    public List<AuditTrail> getAllAuditsByFilter(AuditTrailFilters filters) {
        return auditTrailRepository.findAuditsByFilter(filters);
    }

    public void saveAudit(Employee employee, HttpServletRequest request) {
        String ipAddress = request.getHeader("X-FORWARDED-FOR");
        if (ipAddress == null) {
            ipAddress = request.getRemoteAddr();
        }
        AuditTrail auditTrail = new AuditTrail();
        auditTrail.setCreateBy("SYSTEM");
        auditTrail.setCreateDate(new Date());
        auditTrail.setEmployee(employee);
        auditTrail.setClientIp(ipAddress);

        auditTrailRepository.save(auditTrail);
    }
}
