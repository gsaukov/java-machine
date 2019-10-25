package com.apps.potok.server.alert;

public class Alert {

    private String symbol;
    private String account;
    private Route route;
    private int val;

    public Alert(String symbol, String account, Route route, int val) {
        this.symbol = symbol;
        this.account = account;
        this.route = route;
        this.val = val;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public int getVal() {
        return val;
    }

    public void setVal(int val) {
        this.val = val;
    }

    public static enum Route {
        BUY,
        SELL;
    }
}
