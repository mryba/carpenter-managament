package com.carpenter.core.control.service.offer;

import com.carpenter.core.control.service.login.PrincipalBean;
import com.carpenter.core.control.service.pagination.Pagination;
import com.carpenter.core.control.service.pagination.PaginationService;
import com.carpenter.core.entity.Offer;
import lombok.Getter;

import javax.annotation.PostConstruct;
import javax.faces.component.UICommand;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Named("offerListBean")
@ViewScoped
public class OfferListBean implements PaginationService<Offer>, Serializable {

    private static final long serialVersionUID = 6642476209563003226L;

    @Inject
    OfferService offerService;

    @Inject
    PrincipalBean principalBean;

    private Pagination<Offer> pagination = new Pagination<>(10, 10);
    private Long newOfferCount;

    @PostConstruct
    public void init() {
        refresh();
    }

    private void refresh() {
        performPagination();
        pagination.getItems().clear();
        pagination.getItems().addAll(offerService.getAllOffersByFilter(principalBean, pagination.getRowsPerPage(), pagination.getCurrentPage()));
        newOfferCount = offerService.getAllNotReadOffersCount(principalBean);
    }

    public Long getUnreadOffersFromLoggedUserCompany() {
        return newOfferCount;
    }

    public void changeToRead(Long id) {
        offerService.changeToRead(id);
    }

    @Override
    public List<Offer> items() {
        return pagination.getItems();
    }

    @Override
    public void performPagination() {
        pagination.defaultPagination(Math.toIntExact(offerService.getOfferCount(principalBean)));
    }

    @Override
    public void firstPage() {
        page(0);
    }

    @Override
    public void nextPage() {
        page(pagination.getFirstRow() + pagination.getRowsPerPage());
    }

    @Override
    public void previousPage() {
        page(pagination.getFirstRow() - pagination.getRowsPerPage());
    }

    @Override
    public void lastPage() {
        page(pagination.getTotalRows() - ((pagination.getTotalRows() % pagination.getRowsPerPage() != 0) ? pagination.getTotalRows() % pagination.getRowsPerPage() : pagination.getRowsPerPage()));

    }

    @Override
    public void page(int firstRow) {
        this.pagination.setFirstRow(firstRow);
        init();
    }

    @Override
    public void page(ActionEvent event) {
        page(((Integer) ((UICommand) event.getComponent()).getValue() - 1) * pagination.getRowsPerPage());

    }

    @Override
    public List<String> getIterationList() {
        List<String> results = new ArrayList<>();
        for (int i = 5; i < 35; i += 5) {
            results.add(String.valueOf(i));
        }
        return results;
    }
}
