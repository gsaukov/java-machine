package com.apps.potok.soketio.config;

import com.apps.potok.exchange.core.Order;
import com.apps.potok.exchange.core.Position;
import com.apps.potok.exchange.core.SymbolContainer;
import com.apps.potok.soketio.model.LogLine;
import com.apps.potok.soketio.model.execution.PositionNotification;
import com.apps.potok.exchange.account.Account;
import com.apps.potok.exchange.account.AccountManager;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.ConnectListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.session.data.redis.RedisOperationsSessionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.apps.potok.soketio.config.SessionUtil.ACCOUNT_ID;
import static com.apps.potok.exchange.account.AccountManager.TEST_ACCOUNT_ID;

@Service
public class SessionConnectListener implements ConnectListener {

    @Value("${session.test-mode-authentication}")
    private boolean testModeAuthentication;

    @Autowired
    private RedisOperationsSessionRepository sessionRepository;

    @Autowired
    private SymbolContainer symbolContainer;

    @Autowired
    private AccountManager accountManager;

    @Override
    public void onConnect(SocketIOClient client) {
        Account account = assignAccountId(client);
        sendClientSymbols(client);
        sendBalance(account, client);
        sendPositions(account, client);
        sendOrders(account, client);
    }

    private Account assignAccountId(SocketIOClient client){
        String accountId = null;
        if(testModeAuthentication){
            accountId = TEST_ACCOUNT_ID;
        } else {
            OAuth2Authentication oAuth2Authentication = SessionUtil.getOAuth2Authentication(client.getHandshakeData(),
                    sessionRepository);
            accountId = getAccountId(oAuth2Authentication.getUserAuthentication());
        }
        client.set(ACCOUNT_ID, accountId);
        return accountManager.addClient(accountId, client.getSessionId());
    }

    private String getAccountId(Authentication auth){
        for (GrantedAuthority grantedAuth : auth.getAuthorities()) {
            if (grantedAuth.getAuthority().startsWith(ACCOUNT_ID)) {
                return grantedAuth.getAuthority().substring(ACCOUNT_ID.length());
            }
        }
        return null;
    }

    private void sendClientSymbols(SocketIOClient client) {
        client.sendEvent("message", getTenSymbols());
    }

    private LogLine getTenSymbols() {
        List<String> symols = symbolContainer.getSymbols().subList(0, 10);
        LogLine logLine = new LogLine();
        logLine.setLine(symols.toString());
        return logLine;
    }

    private void sendBalance(Account account, SocketIOClient client) {
        client.sendEvent("balance", account.getBalance());
    }

    private void sendPositions(Account account, SocketIOClient client) {
        for(Position position : account.getPositions()) {
            PositionNotification notification = new PositionNotification(position);
            client.sendEvent("positionNotification", notification);
        }
        for(Position position : account.getShortPositions()) {
            PositionNotification notification = new PositionNotification(position);
            client.sendEvent("positionNotification", notification);
        }
    }

    private void sendOrders(Account account, SocketIOClient client) {
        for(Order order : account.getOrders()) {
            client.sendEvent("orderConfirm", order);
        }
    }
}
