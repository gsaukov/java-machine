package com.apps.potok.exchange.core;

import com.apps.potok.exchange.account.Account;
import com.apps.potok.exchange.account.AccountManager;
import com.apps.potok.exchange.notifiers.BalanceNotifier;
import com.apps.potok.exchange.notifiers.PositionNotifier;
import com.apps.potok.soketio.model.execution.Execution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.apps.potok.exchange.core.Route.BUY;
import static com.apps.potok.exchange.core.Route.SELL;

@Service
public class ExecutionManager {

    @Autowired
    private BalanceNotifier balanceNotifier;

    @Autowired
    private PositionNotifier positionNotifier;

    @Autowired
    private AccountManager accountManager;

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

}
