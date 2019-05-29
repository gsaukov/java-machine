package com.apps.searchandpagination.persistance.entity;

import org.joda.money.CurrencyUnit;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.math.BigDecimal;

@Embeddable
@Access(AccessType.FIELD)
public class EmbeddableBigMoney {

    @Column(name = "CURRENCY")
    private CurrencyUnit currency;
    @Column(name = "AMOUNT")
    private BigDecimal amount;

    public CurrencyUnit getCurrency() {
        return currency;
    }

    public void setCurrency(CurrencyUnit currency) {
        this.currency = currency;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

}
