package com.apps.potok.soketio.model.execution;

import com.apps.potok.exchange.mkdata.Route;

import java.util.Date;
import java.util.UUID;

public interface Accountable {

    public UUID getUuid();

    public Date getTimestamp();

    public String getSymbol();

    public String getAccountId();

    public Route getRoute();

    public Integer getFillPrice();

    public Integer getBlockedPrice();

    public Integer getQuantity();

    public boolean isDeposit();
}
