package com.apps.potok.config;

import com.apps.potok.exchange.account.Account;

public class ExchangeCondition {
    private Account exchangeAccount;
    private String symbol;
    private int tiers;
    private int askPrice;
    private int bidPrice;
    private int volume;

    public ExchangeCondition(Account exchangeAccount, String symbol, int tiers, int askPrice, int bidPrice, int volume) {
        this.exchangeAccount = exchangeAccount;
        this.symbol = symbol;
        this.tiers = tiers;
        this.askPrice = askPrice;
        this.bidPrice = bidPrice;
        this.volume = volume;
    }

    public Account getExchangeAccount() {
        return exchangeAccount;
    }

    public String getSymbol() {
        return symbol;
    }

    public int getTiers() {
        return tiers;
    }

    public int getAskPrice() {
        return askPrice;
    }

    public int getBidPrice() {
        return bidPrice;
    }

    public int getVolume() {
        return volume;
    }
}
