package com.carpenter.api.endpoint;

import com.carpenter.api.request.CarpenterOfferRequest;
import com.carpenter.api.response.CarpenterOfferResponse;
import com.carpenter.core.control.mail.MailDispatchBean;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Path("/carpenter")
@RequestScoped
@Slf4j
public class CarpenterWebEndpoint {

    @Inject
    MailDispatchBean mailDispatchBean;

    @POST
    @Path("/post")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response sendMail(CarpenterOfferRequest request) {

        Instant instant = Instant.parse(request.getStartDate());

        LocalDate localDate = LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).toLocalDate();
        String sb = "Rodzaj architektury: " + request.getArchType() + "\n" +
                "Imię: " + request.getName() + "\n" +
                "Firma: " + request.getCompany() + "\n" +
                "Telefon: " + request.getPhone() + "\n" +
                "Email: " + request.getEmail() + "\n" +
                "Opis: " + request.getDescription();
        mailDispatchBean.sandEmailToManager(sb);

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
