package com.apps.potok.server.exchange;

import com.apps.potok.server.mkdata.Route;

public class Order {

    private final String symbol;
    private final String account;
    private final Route route;
    private final Integer val;
    private final Integer volume;

    public Order(String symbol, String account, Route route, Integer val, Integer volume) {
        this.symbol = symbol;
        this.account = account;
        this.route = route;
        this.val = val;
        this.volume = volume;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getAccount() {
        return account;
    }

    public Route getRoute() {
        return route;
    }

    public Integer getVal() {
        return val;
    }

    public Integer getVolume() {
        return volume;
    }


}
