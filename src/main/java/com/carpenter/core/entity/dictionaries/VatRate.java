package com.carpenter.core.entity.dictionaries;

import java.math.BigDecimal;

public enum VatRate {

    ZERO(BigDecimal.ZERO),
    FIVE(BigDecimal.valueOf(0.5)),
    EIGHT(BigDecimal.valueOf(0.8)),
    TWENTY_THREE(BigDecimal.valueOf(0.23));

    private BigDecimal rate;

    VatRate(BigDecimal rate) {
        this.rate = rate;
    }

    public BigDecimal getRate() {
        return rate;
    }
}
