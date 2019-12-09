package com.apps.potok.config;

import com.apps.potok.exchange.account.Account;
import com.apps.potok.exchange.core.Order;
import com.apps.potok.soketio.model.order.NewOrder;

import java.util.ArrayList;
import java.util.List;

public class TestScenario {
    private String symbol;
    private Account account;
    private List<NewOrder> newOrders = new ArrayList();
    private List<Order> orders = new ArrayList();
    private List<ExchangeCondition> exchangeConditions = new ArrayList();

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public List<NewOrder> getNewOrders() {
        return newOrders;
    }

    public void addNewOrder(NewOrder newOrder) {
        this.newOrders.add(newOrder);
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void addOrder(Order order) {
        this.orders.add(order);
    }

    public List<ExchangeCondition> getExchangeConditions() {
        return this.exchangeConditions;
    }

    public void addExchangeCondition(ExchangeCondition exchangeCondition) {
        this.exchangeConditions.add(exchangeCondition);
    }

    public void addExchangeConditions(List<ExchangeCondition> exchangeConditions) {
        this.exchangeConditions.addAll(exchangeConditions);
    }

    public String getAccountId() {
        return account.getAccountId();
    }

    public long getBalance() {
        return account.getBalance();
    }

}
