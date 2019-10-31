package com.apps.potok.server.mkdata;

public class MkData {
    private final String symbol;
    private final int val;
    private final Route route;
    private final String account;

    public MkData(String symbol, int val, Route route, String account) {
        this.symbol = symbol;
        this.val = val;
        this.route = route;
        this.account = account;
    }

    public String getSymbol() {
        return symbol;
    }

    public int getVal() {
        return val;
    }

    public Route getRoute() {
        return route;
    }

    public String getAccount() {
        return account;
    }
}
