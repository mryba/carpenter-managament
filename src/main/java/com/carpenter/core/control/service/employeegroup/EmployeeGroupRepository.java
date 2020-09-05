package com.carpenter.core.control.service.employeegroup;

import lombok.extern.slf4j.Slf4j;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;

@Slf4j
@Stateless
public class EmployeeGroupRepository implements Serializable {

    private static final long serialVersionUID = 898047272514883917L;

    @PersistenceContext
    private EntityManager entityManager;


}
