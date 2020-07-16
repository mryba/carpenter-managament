package com.carpenter.core.control.service.offer;

import com.carpenter.api.request.CarpenterOfferRequest;
import com.carpenter.api.response.CarpenterOfferResponse;
import com.carpenter.core.control.dto.OfferDto;
import com.carpenter.core.control.mail.MailDispatchBean;
import com.carpenter.core.control.repository.OfferRepository;
import com.carpenter.core.entity.Offer;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Slf4j
@SessionScoped
public class OfferService implements Serializable {

    @Inject
    private OfferRepository offerRepository;

    @Inject
    private MailDispatchBean mailDispatchBean;

    private OfferMapper offerMapper;

    public Offer create(OfferDto offerDto) {
        offerMapper = new OfferMapper();
        return offerMapper.mapFromDomain(offerDto);
    }

    public void save(Offer offer) {
        offerRepository.save(offer);
    }

    public void remove(Long id) {
        offerRepository.remove(id);
    }

    public void changeToRead(Long id) {
        offerRepository.changeToRead(id);
    }

    public List<Offer> getOffersByCompany(Long companyId) {
        if (companyId == null) {
            return null;
        }
        return offerRepository.getOffersByCompany(companyId);
    }

    public Offer findOfferById(Long id) {
        return offerRepository.findOfferById(id);
    }

    public String performOffer(CarpenterOfferRequest request) {
        Instant instant = Instant.parse(request.getStartDate());
        offerMapper = new OfferMapper();

        LocalDate workDateFrom = LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).toLocalDate();

        OfferDto offerDto = OfferDto.builder()
                .architectureType(request.getArchType())
                .workCity(request.getCity())
                .workDateFrom(Date.from(instant))
                .forenameOfCalling(request.getName())
                .companyName(request.getCompany().isEmpty() ? null : request.getCompany())
                .email(request.getEmail())
                .phone(request.getPhone().isEmpty() ? null : request.getPhone())
                .description(request.getDescription())
                .build();
        Offer offer = offerMapper.mapFromDomain(offerDto);
        offer.setCreateDate(new Date());
        offer.setCreateBy("system");

        offerRepository.save(offer);
        log.info("Successfully saved offer: {}", offer);

        String sb = "<html><body>"
                + "<table style='display:block'>" +
                "<tr><td>" +
                "Miasto: " + request.getCity() +
                "</tr></td>" +
                "<tr><td>" +
                "Rodzaj architektury: " + request.getArchType() +
                "</tr></td>" +
                "<tr><td>" +
                "Imię: " + request.getName() +
                "</tr></td>" +
                "<tr><td>" +
                "Firma: " + request.getCompany() +
                "</tr></td>" +
                "<tr><td>" +
                "Telefon: " + request.getPhone() +
                "</tr></td>" +
                "<tr><td>" +
                "Email: " + request.getEmail() +
                "</tr></td>" +
                "<tr><td>" +
                "Opis: " + request.getDescription() +
                "</tr></td>" +
                "</table></body></html>";
        mailDispatchBean.sandEmailToManager(sb);

        CarpenterOfferResponse response = new CarpenterOfferResponse(Boolean.TRUE, Boolean.TRUE);
        return new Gson().toJson(response);
    }
}
