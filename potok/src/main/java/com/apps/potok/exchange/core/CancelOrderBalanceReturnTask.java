package com.apps.potok.exchange.core;

import com.apps.potok.exchange.notifiers.BalanceNotifier;
import com.apps.potok.exchange.account.Account;

import static com.apps.potok.exchange.core.Route.BUY;
import static com.apps.potok.exchange.core.Route.SELL;

public class CancelOrderBalanceReturnTask implements Runnable {

    private final Order canceledOrder;
    private final Account account;
    private final BalanceNotifier balanceNotifier;

    public CancelOrderBalanceReturnTask(Order canceledOrder, Account account, BalanceNotifier balanceNotifier) {
        this.canceledOrder = canceledOrder;
        this.account = account;
        this.balanceNotifier = balanceNotifier;
    }

    @Override
    public void run() {
        if(SELL.equals(canceledOrder.getRoute())){
            cancelSellOrderBalanceProcessor(canceledOrder, account);
        } else if (BUY.equals(canceledOrder.getRoute())) {
            cancelBuyOrderBalanceProcessor(canceledOrder, account);
        } else { //short
            cancelShortOrderBalanceProcessor(canceledOrder, account);
        }
        balanceNotifier.pushBalance(account);
    }

    private void cancelBuyOrderBalanceProcessor(Order canceledOrder, Account account) {
        long balanceChange = canceledOrder.getVal() * canceledOrder.getVolume();
        account.doPositiveOrderBalance(balanceChange);
    }

    private void cancelShortOrderBalanceProcessor(Order canceledOrder, Account account) {
        long balanceChange = canceledOrder.getBlockedPrice() * canceledOrder.getVolume();
        account.doPositiveOrderBalance(balanceChange);
    }

    private void cancelSellOrderBalanceProcessor(Order canceledOrder, Account account) {
        //Sell orders do not block any balance so there is nothing to return.
    }

}
