package com.carpenter.core.staff_member.boundary;

import com.carpenter.core.staff_member.entity.Employer;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/login")
public class LoginRestController {

    @PersistenceContext
    EntityManager entityManager;

    @POST
    @Path("/create")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createNewUser(Employer employer) {
        entityManager.persist(employer);
        return Response.accepted(employer).build();
    }
}
