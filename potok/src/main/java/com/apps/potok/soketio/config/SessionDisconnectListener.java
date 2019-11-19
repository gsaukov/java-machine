package com.apps.potok.soketio.config;

import com.apps.potok.soketio.server.AccountContainer;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.DisconnectListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static com.apps.potok.soketio.config.SessionUtil.getAccountId;

@Service
public class SessionDisconnectListener implements DisconnectListener {

    @Value("${session.test-mode-authentication}")
    private boolean testModeAuthentication;

    @Autowired
    private AccountContainer accountContainer;

    @Override
    public void onDisconnect(SocketIOClient client) {
        removeAccountId(client);
    }

    private void removeAccountId(SocketIOClient client){
        String accountId = getAccountId(client);
        accountContainer.removeAccountClient(accountId, client.getSessionId());
    }
}
