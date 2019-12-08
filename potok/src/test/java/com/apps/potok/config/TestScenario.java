package com.apps.potok.config;

import com.apps.potok.exchange.account.Account;
import com.apps.potok.exchange.core.Order;
import com.apps.potok.exchange.mkdata.Route;
import com.apps.potok.soketio.model.order.NewOrder;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.ArrayList;
import java.util.List;

public class TestScenario {
    private String symbol;
    private Account account;
    private List<NewOrder> newOrders = new ArrayList();
    private List<Order> orders = new ArrayList();

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




}
