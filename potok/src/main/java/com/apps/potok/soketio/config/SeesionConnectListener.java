package com.apps.potok.soketio.config;

import com.apps.potok.exchange.core.SymbolContainer;
import com.apps.potok.soketio.model.LogFile;
import com.apps.potok.soketio.server.AccountContainer;
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

@Service
public class SeesionConnectListener implements ConnectListener {

    public static final String ACCOUNT_ID = "ACCOUNT_ID";

    @Value("${session.test-mode-authentication}")
    private boolean testModeAuthentication;

    @Autowired
    private RedisOperationsSessionRepository sessionRepository;

    @Autowired
    private SymbolContainer symbolContainer;

    @Autowired
    private AccountContainer accountContainer;

    @Override
    public void onConnect(SocketIOClient client) {
        assignAccountId(client);
        sendClientSymbols(client);
    }

    private void assignAccountId(SocketIOClient client){
        String accountId = null;
        if(testModeAuthentication){
            accountId = "test_account_id";
        } else {
            OAuth2Authentication oAuth2Authentication = SessionUtil.getOAuth2Authentication(client.getHandshakeData(),
                    sessionRepository);
            accountId = getAccountId(oAuth2Authentication.getUserAuthentication());
        }
        client.set(ACCOUNT_ID, accountId);
        accountContainer.addAccount(accountId);
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

    private LogFile getTenSymbols() {
        List<String> symols = symbolContainer.getSymbols().subList(0, 10);
        LogFile logFile = new LogFile();
        logFile.setLine(symols.toString());
        return logFile;
    }
}
