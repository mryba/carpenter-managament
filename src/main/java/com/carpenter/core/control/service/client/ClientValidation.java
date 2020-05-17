package com.carpenter.core.control.service.client;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

@ViewScoped
@Named("clientValidation")
public class ClientValidation implements Serializable {
    private final static long serialVersionUID = 4912365403594665L;

    @Inject ClientService clientService;

    public void validateEmptyField() {

    }

    public void validateNip() {

    }


}
