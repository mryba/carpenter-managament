package com.carpenter.core.control.service.offer;

import com.carpenter.core.control.service.common.Filters;

import javax.enterprise.event.Event;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

@ViewScoped
@Named("offerFilters")
public class OfferFilters extends Filters implements Serializable {

    private static final long serialVersionUID = 9152281880274979469L;

    @Inject
    private Event<OfferFilters> event;
}
