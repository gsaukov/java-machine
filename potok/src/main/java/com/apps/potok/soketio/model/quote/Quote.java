package com.apps.potok.soketio.model.quote;

import com.apps.potok.exchange.core.Route;

import java.io.Serializable;

public class Quote implements Serializable {

    private final String symbol;
    private final Integer price;
    private final Integer volume;
    private final Route route;

    public Quote(String symbol, Integer price, Integer volume, Route route) {
        this.symbol = symbol;
        this.price = price;
        this.volume = volume;
        this.route = route;
    }

    public String getSymbol() {
        return symbol;
    }

    public Integer getPrice() {
        return price;
    }

    public Integer getVolume() {
        return volume;
    }

    public Route getRoute() {
        return route;
    }
}
