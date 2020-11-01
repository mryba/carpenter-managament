package com.carpenter.core.control.service.offer;

import com.carpenter.api.request.CarpenterOfferRequest;
import com.carpenter.api.response.CarpenterOfferResponse;
import com.carpenter.core.control.dto.OfferDto;
import com.carpenter.core.control.mail.EmailStorageBean;
import com.carpenter.core.control.repository.OfferRepository;
import com.carpenter.core.control.service.company.CompanyService;
import com.carpenter.core.control.service.login.PrincipalBean;
import com.carpenter.core.entity.Company;
import com.carpenter.core.entity.Offer;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Stateless
public class OfferService implements Serializable {

    private static final long serialVersionUID = -233684878971707271L;

    @Inject
    private OfferRepository offerRepository;

    @Inject
    private EmailStorageBean emailStorageBean;

    @Inject
    private CompanyService companyService;

    private OfferMapper offerMapper;

    public void changeToRead(Long id) {
        offerRepository.changeToRead(id);
    }

    public List<OfferDto> getAllOffers() {
        offerMapper = new OfferMapper();
        return offerRepository.getAllOffers().stream().map(offerMapper::mapToDomain).collect(Collectors.toList());
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

        emailStorageBean.saveEmail(OfferHtmlTemplate.offerTemplateHtml(request, workDateFrom));
        log.info("Successfully saved email");

        CarpenterOfferResponse response = new CarpenterOfferResponse(Boolean.TRUE, Boolean.TRUE);
        return new Gson().toJson(response);
    }

    public Collection<Offer> getAllOffersByFilter(PrincipalBean principalBean, int rowsPerPage, int currentPage) {
        return offerRepository.findAllOffersByFilter(principalBean, rowsPerPage, currentPage);
    }

    public Long getOfferCount(PrincipalBean principalBean) {
        return offerRepository.countAllOffersByFilter(principalBean);
    }

    public Long getAllNotReadOffersCount(PrincipalBean principalBean) {
        return offerRepository.countNotReadOffers(principalBean);
    }
}
