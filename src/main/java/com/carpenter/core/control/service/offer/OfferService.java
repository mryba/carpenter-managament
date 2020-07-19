package com.carpenter.core.control.service.offer;

import com.carpenter.api.request.CarpenterOfferRequest;
import com.carpenter.api.response.CarpenterOfferResponse;
import com.carpenter.core.control.dto.OfferDto;
import com.carpenter.core.control.mail.MailDispatchBean;
import com.carpenter.core.control.repository.OfferRepository;
import com.carpenter.core.control.service.company.CompanyService;
import com.carpenter.core.entity.Company;
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
import java.util.stream.Collectors;

@Slf4j
@SessionScoped
public class OfferService implements Serializable {

    @Inject
    private OfferRepository offerRepository;

    @Inject
    private MailDispatchBean mailDispatchBean;

    @Inject
    private CompanyService companyService;

    private OfferMapper offerMapper;

    public void changeToRead(Long id) {
        offerRepository.changeToRead(id);
    }

    public List<OfferDto> getOffersByCompany(Long companyId) {
        offerMapper = new OfferMapper();
        return offerRepository.getOffersByCompany(companyId)
                .stream()
                .map(offerMapper::mapToDomain)
                .collect(Collectors.toList());
    }

    public Offer findOfferById(Long id) {
        return offerRepository.findOfferById(id);
    }

    public String performOffer(CarpenterOfferRequest request) {
        Instant instant = Instant.parse(request.getStartDate());
        LocalDate workDateFrom = LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).toLocalDate();

        offerMapper = new OfferMapper();
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

        //for now
        Company company = companyService.getCompanyById(2L);

        //after deploy
//        Company company = companyService.getCompanyByName("Podkarpaccy Cieśla");

        company.addOffer(offer);
        companyService.saveCompany(company);
        log.info("Successfully saved offer");

        mailDispatchBean.sandEmailToManager(OfferHtmlTemplate.offerTemplateHtml(request, workDateFrom));

        CarpenterOfferResponse response = new CarpenterOfferResponse(Boolean.TRUE, Boolean.TRUE);
        return new Gson().toJson(response);
    }
}
