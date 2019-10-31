package com.apps.potok.soketio.model.quote;

import com.apps.potok.server.mkdata.Route;

import java.io.Serializable;

public class Quote implements Serializable {

    private final String symbol;
    private final Integer price;
    private final Route route;

    public Quote(String symbol, Integer price, Route route) {
        this.symbol = symbol;
        this.price = price;
        this.route = route;
    }

    public String getSymbol() {
        return symbol;
    }

    public Integer getPrice() {
        return price;
    }

    public Route getRoute() {
        return route;
    }
}
