package com.carpenter.core.control.service.offer;

import com.carpenter.api.request.CarpenterOfferRequest;

import java.time.LocalDate;

public class OfferHtmlTemplate {

    private OfferHtmlTemplate() {

    }

    public static String offerTemplateHtml(CarpenterOfferRequest request, LocalDate workDateStart) {
        return "<html><body>"
                + "<table style='display:block'>" +
                "<tr><td>" +
                "Miasto: " + "<strong>" + request.getCity() + "</strong>" +
                "</tr></td>" +
                "<tr><td>" +
                "Rodzaj architektury: " + "<strong>" + request.getArchType() + "</strong>" +
                "</tr></td>" +
                "<tr><td>" +
                "Data rozpoczęcia: " + "<strong>" + workDateStart + "</strong>" +
                "</tr></td>" +
                "<tr><td>" +
                "Imię: " + "<strong>" + request.getName() + "</strong>" +
                "</tr></td>" +
                "<tr><td>" +
                "Firma: " + "<strong>" + request.getCompany() + "</strong>" +
                "</tr></td>" +
                "<tr><td>" +
                "Telefon: " + "<strong>" + request.getPhone() + "</strong>" +
                "</tr></td>" +
                "<tr><td>" +
                "Email: " + "<strong>" + request.getEmail() + "</strong>" +
                "</tr></td>" +
                "<tr><td>" +
                "Opis: " + "<strong>" + request.getDescription() + "</strong>" +
                "</tr></td>" +
                "</table></body></html>";
    }
}
