package com.carpenter.api.endpoint;

import com.carpenter.api.request.CarpenterOfferRequest;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.RequestScoped;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/carpenter")
@RequestScoped
@Slf4j
public class CarpenterWebEndpoint {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @NotNull
    public Response sendMail(CarpenterOfferRequest request) {

        return Response.status(Response.Status.OK).build();
    }
}
