package com.carpenter.core.control.service.employeegroup;

import com.carpenter.core.entity.EmployeeGroup;
import lombok.extern.slf4j.Slf4j;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.List;

@Slf4j
@Stateless
public class EmployeeGroupService implements Serializable {

    private static final long serialVersionUID = -7304092710909296446L;

    @Inject
    private EmployeeGroupRepository employeeGroupRepository;

    public List<EmployeeGroup> getAllEmployeeGroups() {
        return employeeGroupRepository.findAllEmployeeGroups();
    }

    public void saveGroup(EmployeeGroup employeeGroup) {
        employeeGroupRepository.save(employeeGroup);
    }

    public void deleteGroup(EmployeeGroup employeeGroup) {
        employeeGroupRepository.removeGroup(employeeGroup);
    }
}
