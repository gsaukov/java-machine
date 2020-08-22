package com.apps.potok.soketio.model.execution;

import com.apps.potok.exchange.core.Position;
import com.apps.potok.exchange.core.Route;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

public class PositionNotification implements Serializable {

    private final UUID uuid;
    private final Date createdTimestamp;
    private final String symbol;
    private final Route route;
    private final String account;
    private final Integer volume;
    private final Double weightedAveragePrice;
    private final Double averagePerformance;

    public PositionNotification(Position position) {
        this.uuid = position.getUuid();
        this.createdTimestamp = position.getCreatedTimestamp();
        this.symbol = position.getSymbol();
        this.route = position.getRoute();
        this.account = position.getAccountId();
        this.volume = position.getVolume();
        this.weightedAveragePrice = position.calculateWeightedAveragePrice();
        this.averagePerformance = position.calculateAveragePerformance();
    }

    public UUID getUuid() {
        return uuid;
    }

    public Date getCreatedTimestamp() {
        return createdTimestamp;
    }

    public String getSymbol() {
        return symbol;
    }

    public Route getRoute() {
        return route;
    }

    public String getAccount() {
        return account;
    }

    public Integer getVolume() {
        return volume;
    }

    public Double getWeightedAveragePrice() {
        return weightedAveragePrice;
    }

    public Double getAveragePerformance() {
        return averagePerformance;
    }

}
