package com.apps.potok.exchange.core;

import com.apps.potok.exchange.account.Account;
import com.apps.potok.exchange.notifiers.BalanceNotifier;
import com.apps.potok.exchange.notifiers.PositionNotifier;
import com.apps.potok.soketio.model.execution.CloseShortPosition;
import com.apps.potok.soketio.model.execution.CloseShortPositionRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.apps.potok.exchange.core.Route.BUY;
import static com.apps.potok.exchange.core.Route.SHORT;

@Service
public class CloseShortManager {

    @Autowired
    private BalanceNotifier balanceNotifier;

    @Autowired
    private PositionNotifier positionNotifier;

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


}
