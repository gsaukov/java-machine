package com.apps.potok.exchange.core;

import com.apps.potok.exchange.account.Account;
import com.apps.potok.exchange.account.AccountManager;
import com.apps.potok.exchange.notifiers.BalanceNotifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.apps.potok.exchange.core.Route.BUY;

@Service
public class CancelOrderManager {

    @Value("${exchange.cancel-balance-return-delay}")
    private Integer cancelBalanceReturnDelay;

    private final ScheduledExecutorService executorService;
    private final AskContainer askContainer;
    private final BidContainer bidContainer;

    @Autowired
    private AccountManager accountManager;

    @Autowired
    private BalanceNotifier balanceNotifier;

    public CancelOrderManager(AskContainer askContainer, BidContainer bidContainer) {
        this.askContainer = askContainer;
        this.bidContainer = bidContainer;
        this.executorService = Executors.newScheduledThreadPool(1);
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
            executorService.schedule(createCancelOrderBalanceReturnTask(orderToRemove, account), cancelBalanceReturnDelay, TimeUnit.MILLISECONDS);
        }
        return orderToRemove;
    }

    private CancelOrderBalanceReturnTask createCancelOrderBalanceReturnTask(Order order, Account account) {
        return new CancelOrderBalanceReturnTask(order, account, this.balanceNotifier);
    }

}
