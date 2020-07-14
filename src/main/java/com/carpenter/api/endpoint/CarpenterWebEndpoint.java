package com.carpenter.api.endpoint;

import com.carpenter.api.request.CarpenterOfferRequest;
import com.carpenter.api.response.CarpenterOfferResponse;
import com.google.gson.Gson;
import com.sun.corba.se.impl.orbutil.ObjectWriter;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.RequestScoped;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.CacheResponse;

@Path("/carpenter")
@RequestScoped
@Slf4j
public class CarpenterWebEndpoint {

    @POST
    @Path("/post")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response sendMail(CarpenterOfferRequest request) {

        CarpenterOfferResponse response = new CarpenterOfferResponse(Boolean.TRUE, Boolean.TRUE);
        String json = new Gson().toJson(response);

        return Response
                .ok(json, MediaType.APPLICATION_JSON)
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Credentials", "true")
                .header("Access-Control-Allow-Headers",
                        "origin, content-type, accept, authorization")
                .header("Access-Control-Allow-Methods",
                        "GET, POST, PUT, DELETE, OPTIONS, HEAD")
                .build();
    }

    @OPTIONS
    @Path("{path : .*}")
    public Response options() {
        return Response
                .status(200)
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization")
                .header("Access-Control-Allow-Credentials", "true")
                .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD")
                .build();
    }

    @GET
    @Path("/hi")
    @Produces(MediaType.TEXT_PLAIN)
    public Response hi() {
        return Response.status(Response.Status.OK).build();
    }
}
