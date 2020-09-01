package com.apps.potok.exchange.core;

import com.apps.potok.exchange.notifiers.BalanceNotifier;
import com.apps.potok.soketio.model.order.NewOrder;
import com.apps.potok.exchange.account.Account;
import com.apps.potok.exchange.account.AccountManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static com.apps.potok.exchange.core.Route.BUY;
import static com.apps.potok.exchange.core.Route.SELL;

@Service
public class OrderManager {

    private final int RISK_FACTOR = 10;

    private final ConcurrentHashMap<UUID, Order> orderPool;

    @Autowired
    private SymbolContainer symbolContainer;

    @Autowired
    private AccountManager accountManager;

    @Autowired
    private BalanceNotifier balanceNotifier;

    @Autowired
    private Exchange exchange;

    public OrderManager() {
        this.orderPool = new ConcurrentHashMap();
    }

    public Order addOrder(Order order) {
        Account account = accountManager.getAccount(order.getAccount());
        account.addOrder(order);
        orderPool.put(order.getUuid(), order);
        return order;
    }

    public Order manageNew(NewOrder newOrder, Account account) {
        Route route = getRoute(newOrder);
        Order order = null;
        if (SELL.equals(route)) {
            order = newSellOrderBalanceProcessor(newOrder, account, route);
        } else if (BUY.equals(route)) {
            order = buyOrderBalanceProcessor(newOrder, account, route);
        } else { //short
            order = shortOrderBalanceProcessor(newOrder, account, route);
        }
        if(order != null){
            addOrder(order);
            exchange.fireOrder(order);
        }
        return order;
    }

    private Order buyOrderBalanceProcessor(NewOrder newOrder, Account account, Route route) {
        long predictedAmount = newOrder.getVolume() * symbolContainer.getQuote(newOrder.getSymbol());
        long balanceRisk = predictedAmount + predictedAmount / RISK_FACTOR;
        long balanceChange = newOrder.getVolume() * newOrder.getVal();
        boolean success = account.doNegativeOrderBalance(balanceRisk, balanceChange);
        if (success) {
            balanceNotifier.pushBalance(account);
            return createOrder(newOrder, account, route);
        } else {
            return null;
        }
    }

    private Order shortOrderBalanceProcessor(NewOrder newOrder, Account account, Route route) {
        Integer blockedPrice = getShortBlockedPrice(newOrder.getSymbol());
        long blockedBalance = newOrder.getVolume() * blockedPrice;
        boolean success = account.doNegativeOrderBalance(blockedBalance, blockedBalance);
        if (success) {
            balanceNotifier.pushBalance(account);
            return createShortOrder(newOrder, account, route, blockedPrice);
        } else {
            return null;
        }
    }

    public Integer getShortBlockedPrice(String symbol) {
        Integer blockedPrice = symbolContainer.getQuote(symbol);
        return blockedPrice + blockedPrice / RISK_FACTOR;
    }

    //todo it is not thread safe for account with many client connections, sell orders could change, needs to be fixed, could
    private Order newSellOrderBalanceProcessor(NewOrder newOrder, Account account, Route route) {
        long existingSellOrder = account.getExistingSellOrderVolume(newOrder.getSymbol());
        long existingPositivePosition = account.getExistingPositivePositionVolume(newOrder.getSymbol());
        long existingPositionDiff = existingPositivePosition - existingSellOrder;
        long newPositionDiff = existingPositionDiff - newOrder.getVolume();
        if (newPositionDiff < 0) {
            return null; //disallow short on sell.
        } else { // closing existing position
            return createOrder(newOrder, account, route);
        }
    }

    public Order getOrder(UUID orderUuid) {
        return orderPool.get(orderUuid);
    }

    private Order createOrder(NewOrder newOrder, Account account, Route route) {
        return new Order(newOrder.getSymbol(), account.getAccountId(), route, newOrder.getVal(), newOrder.getVolume());
    }

    private Order createShortOrder(NewOrder newOrder, Account account, Route route, Integer blockedBalance) {
        return new Order(newOrder.getSymbol(), account.getAccountId(), route, newOrder.getVal(), newOrder.getVolume(), blockedBalance);
    }

    private Route getRoute(NewOrder newOrder) {
        return Route.valueOf(newOrder.getRoute());
    }
}
