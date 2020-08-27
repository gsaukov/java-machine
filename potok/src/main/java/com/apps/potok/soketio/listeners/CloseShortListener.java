package com.apps.potok.soketio.listeners;

import com.apps.potok.exchange.account.Account;
import com.apps.potok.exchange.account.AccountManager;
import com.apps.potok.exchange.core.CloseShortManager;
import com.apps.potok.soketio.model.execution.CloseShortPosition;
import com.apps.potok.soketio.model.execution.CloseShortPositionRequest;
import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.DataListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.apps.potok.soketio.config.SessionUtil.getAccountId;

@Service
public class CloseShortListener implements DataListener<CloseShortPositionRequest> {

    @Autowired
    private CloseShortManager closeShortManager;

    @Autowired
    private AccountManager accountManager;

    @Override
    public void onData(SocketIOClient client, CloseShortPositionRequest data, AckRequest ackRequest) {
        String accountId = getAccountId(client);
        Account account = accountManager.getAccount(accountId);
        CloseShortPosition closeShortPosition = closeShortManager.manageCloseShort(data, account);
        if (closeShortPosition != null) {
            client.sendEvent("closeShortPosition", closeShortPosition);
        }
    }

}
