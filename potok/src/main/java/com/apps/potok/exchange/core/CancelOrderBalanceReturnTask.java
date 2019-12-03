package com.apps.potok.exchange.core;

import com.apps.potok.exchange.eventhandlers.BalanceNotifierServer;
import com.apps.potok.soketio.server.Account;

import static com.apps.potok.exchange.mkdata.Route.BUY;

public class CancelOrderBalanceReturnTask implements Runnable {

    private final Order canceledOrder;
    private final Account account;
    private final BalanceNotifierServer balanceNotifier;

    public CancelOrderBalanceReturnTask(Order canceledOrder, Account account, BalanceNotifierServer balanceNotifier) {
        this.canceledOrder = canceledOrder;
        this.account = account;
        this.balanceNotifier = balanceNotifier;
    }

    @Override
    public void run() {
        if(BUY.equals(canceledOrder.getRoute())){ //todo cancelBuyShort
            cancelBuyOrderBalanceProcessor(canceledOrder, account);
        } else {
            cancelSellOrderBalanceProcessor(canceledOrder, account);
        }
        balanceNotifier.pushBalance(account);
    }

    private void cancelBuyOrderBalanceProcessor(Order canceledOrder, Account account) {
        long balanceChange = canceledOrder.getVal() * canceledOrder.getVolume();
        account.doPositiveOrderBalance(balanceChange);
    }

    private void cancelSellOrderBalanceProcessor(Order canceledOrder, Account account) {
        //todo return balance
    }

}
