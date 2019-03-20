package com.apps.alertserver;

public class MkData {

    private String symbol;
    private int val;

    public MkData(String symbol, int val) {
        this.symbol = symbol;
        this.val = val;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public int getVal() {
        return val;
    }

    public void setVal(int val) {
        this.val = val;
    }

}
