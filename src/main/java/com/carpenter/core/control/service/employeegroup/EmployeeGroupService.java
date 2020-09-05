package com.carpenter.core.control.service.employeegroup;

import lombok.extern.slf4j.Slf4j;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.io.Serializable;

@Slf4j
@Stateless
public class EmployeeGroupService implements Serializable {

    private static final long serialVersionUID = -7304092710909296446L;

    @Inject
    private EmployeeGroupRepository employeeGroupRepository;
}
