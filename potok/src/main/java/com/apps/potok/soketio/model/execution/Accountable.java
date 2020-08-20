package com.apps.potok.soketio.model.execution;

import com.apps.potok.exchange.core.Route;

import java.util.Date;
import java.util.UUID;

public interface Accountable {

    UUID getUuid();

    Date getTimestamp();

    String getSymbol();

    String getAccountId();

    Route getRoute();

    Integer getFillPrice();

    Integer getBlockedPrice();

    Integer getQuantity();

    boolean isDeposit();
}
