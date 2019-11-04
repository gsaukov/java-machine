package com.apps.potok.server.exchange;

import com.apps.potok.server.mkdata.Route;

public class Order {

    private final String symbol;
    private final String account;
    private final Route route;
    private final int val;

    public Order(String symbol, String account, Route route, int val) {
        this.symbol = symbol;
        this.account = account;
        this.route = route;
        this.val = val;
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

    public int getVal() {
        return val;
    }

}
