package com.carpenter.core.controller;

import com.carpenter.core.model.Employee;
import org.jboss.resteasy.annotations.Body;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/employee")
public class EmployerController {

    @PersistenceContext
    EntityManager entityManager;

    @POST
    public Response createUser(Employee employee) {
        entityManager.persist(employee);
        return Response.accepted().build();
    }


}
