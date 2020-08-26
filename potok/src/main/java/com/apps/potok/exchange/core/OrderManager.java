package com.apps.potok.exchange.core;

import com.apps.potok.exchange.notifiers.BalanceNotifierServer;
import com.apps.potok.exchange.notifiers.PositionNotifierServer;
import com.apps.potok.soketio.model.execution.CloseShortPosition;
import com.apps.potok.soketio.model.execution.CloseShortPositionRequest;
import com.apps.potok.soketio.model.execution.Execution;
import com.apps.potok.soketio.model.order.NewOrder;
import com.apps.potok.exchange.account.Account;
import com.apps.potok.exchange.account.AccountManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.apps.potok.exchange.core.Route.BUY;
import static com.apps.potok.exchange.core.Route.SELL;
import static com.apps.potok.exchange.core.Route.SHORT;

@Service
public class OrderManager {

    private final int RISK_FACTOR = 10;

    private final ScheduledExecutorService executorService;
    private final ConcurrentHashMap<UUID, Order> orderPool;
    private final AskContainer askContainer;
    private final BidContainer bidContainer;

    @Value("${exchange.cancel-balance-return-delay}")
    private Integer cancelBalanceReturnDelay;

    @Autowired
    private SymbolContainer symbolContainer;

    @Autowired
    private AccountManager accountManager;

    @Autowired
    private BalanceNotifierServer balanceNotifier;

    @Autowired
    private PositionNotifierServer positionNotifier;

    public OrderManager(BidContainer bidContainer, AskContainer askContainer) {
        this.askContainer = askContainer;
        this.bidContainer = bidContainer;
        this.orderPool = new ConcurrentHashMap();
        this.executorService = Executors.newScheduledThreadPool(1);
    }

    public Order manageNew(NewOrder newOrder, Account account) {
        Route route = getRoute(newOrder);
        if (SELL.equals(route)) {
            return newSellOrderBalanceProcessor(newOrder, account, route);
        } else if (BUY.equals(route)) {
            return buyOrderBalanceProcessor(newOrder, account, route);
        } else { //short
            return shortOrderBalanceProcessor(newOrder, account, route);
        }
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

    public Order addOrder(Order order) {
        Account account = accountManager.getAccount(order.getAccount());
        account.addOrder(order);
        orderPool.put(order.getUuid(), order);
        return order;
    }

    // returns removed order, returns null if order is already executed or not found.
    // BUY/SHORT balance on cancel should be returned assuming that it could be executed by other threads so be careful.
    public Order cancelOrder(UUID uuid, String accountId) {
        Account account = accountManager.getAccount(accountId);
        Order orderToRemove = account.getOrder(uuid);
        if (orderToRemove != null && orderToRemove.getAccount().equals(accountId) && orderToRemove.isActive() && orderToRemove.getVolume() > 0) {
            orderToRemove.cancel();
            if (BUY.equals(orderToRemove.getRoute())) {
                askContainer.removeAsk(orderToRemove);
            } else {
                bidContainer.removeBid(orderToRemove);
            }
            executorService.schedule(createCancelOrderBalanceReturnTask(orderToRemove, account), cancelBalanceReturnDelay, TimeUnit.SECONDS);
        }
        return orderToRemove;
    }

    public Order manageExecution(Execution execution) {
        Account account = accountManager.getAccount(execution.getAccountId());
        Order order = account.getOrder(execution.getOrderUuid());
        if (SELL.equals(order.getRoute())) {
            sellExecutionBalanceProcessor(execution, account);
        } else if (BUY.equals(order.getRoute())) {
            buyExecutionBalanceProcessor(execution, order, account);
        } else {
            shortExecutionBalanceProcessor(execution, order, account);
        }
        Position position = account.doExecution(execution);
        positionNotifier.pushPositionNotification(position.getAccountId(), position.getSymbol(), position.getRoute());
        // should be done for down stream processing persistance, accounting, transaction journalization and balance update.
        return order;
    }

    // Buy Short Execution could happen only at same or better price, the abs difference we return to account balance.
    // Example of buying 1 share:
    // |route	| order	| execution	| return absolute	|
    // |buy 	| 40	| 30		| 10				|
    // |short	| 20	| 40		| 20				|

    private void buyExecutionBalanceProcessor(Execution execution, Order order, Account account) {
        if (order.getVal() != execution.getFillPrice()) {
            long returnAmount = (order.getVal() - execution.getFillPrice()) * execution.getQuantity();
            account.doPositiveOrderBalance(returnAmount);
            balanceNotifier.pushBalance(account);
        }
    }

    private void shortExecutionBalanceProcessor(Execution execution, Order order, Account account) {
        long returnAmount = execution.getFillPrice() * execution.getQuantity();
        account.doPositiveOrderBalance(returnAmount);
        balanceNotifier.pushBalance(account);
    }

    private void sellExecutionBalanceProcessor(Execution execution, Account account) {
        long returnAmount = execution.getFillPrice() * execution.getQuantity();
        account.doPositiveOrderBalance(returnAmount);
        balanceNotifier.pushBalance(account);
    }

    public Order getOrder(UUID orderUuid) {
        return orderPool.get(orderUuid);
    }

    public CloseShortPosition manageCloseShort(CloseShortPositionRequest request, Account account) {
        CloseShortPosition closeShortPosition = new CloseShortPosition(account.getAccountId(), request.getSymbol(), request.getAmount());
        boolean success = account.doCloseShortPosition(closeShortPosition);
        if (success){
            balanceNotifier.pushBalance(account);
            positionNotifier.pushPositionNotification(account.getAccountId(), request.getSymbol(), BUY);
            positionNotifier.pushPositionNotification(account.getAccountId(), request.getSymbol(), SHORT);
        }

        return closeShortPosition;
    }

    private Order createOrder(NewOrder newOrder, Account account, Route route) {
        return new Order(newOrder.getSymbol(), account.getAccountId(), route, newOrder.getVal(), newOrder.getVolume());
    }

    private Order createShortOrder(NewOrder newOrder, Account account, Route route, Integer blockedBalance) {
        return new Order(newOrder.getSymbol(), account.getAccountId(), route, newOrder.getVal(), newOrder.getVolume(), blockedBalance);
    }

    private CancelOrderBalanceReturnTask createCancelOrderBalanceReturnTask(Order order, Account account) {
        return new CancelOrderBalanceReturnTask(order, account, this.balanceNotifier);
    }

    private Route getRoute(NewOrder newOrder) {
        return Route.valueOf(newOrder.getRoute());
    }
}
