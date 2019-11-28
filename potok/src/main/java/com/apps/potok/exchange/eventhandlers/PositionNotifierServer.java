package com.apps.potok.exchange.eventhandlers;

import com.apps.potok.exchange.core.AbstractExchangeServer;
import com.apps.potok.exchange.core.Position;
import com.apps.potok.soketio.model.execution.PositionNotification;
import com.apps.potok.soketio.server.Account;
import com.apps.potok.soketio.server.AccountManager;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedDeque;

@Service
public class PositionNotifierServer extends AbstractExchangeServer {

    private AccountManager accountManager;
    private SocketIOServer server;
    private final ConcurrentLinkedDeque<SymbolAccount> eventQueue = new ConcurrentLinkedDeque<>();

    public PositionNotifierServer(SocketIOServer server, AccountManager accountManager) {
        super.setName("PositionNotifierServer");
        this.server = server;
        this.accountManager = accountManager;
    }

    @Override
    public void runExchangeServer() {
        SymbolAccount symbolAccount = eventQueue.poll();
        if(symbolAccount != null){
            notifyClients(symbolAccount);
        }
    }

    @Override
    public void speedControl() {}

    private void notifyClients(SymbolAccount symbolAccount) {
        Account account = getAccount(symbolAccount.getAccountId());
        List<UUID> clients = account.getClientUuids();
        if(clients != null && !clients.isEmpty()){
            Position position = account.getPosition(symbolAccount.getSymbol());
            PositionNotification notification = new PositionNotification(position);
            for(UUID clientUuid : clients){
                SocketIOClient client = server.getClient(clientUuid);
                if (client != null){
                    client.sendEvent("positionNotification", notification);
                }
            }
        }
    }

    public void pushPositionNotification(String accountId, String symbol) {
        eventQueue.offer(new SymbolAccount(accountId, symbol));
    }

    private Account getAccount(String accountId) {
        return accountManager.getAccount(accountId);
    }

    private static class SymbolAccount {

        private final String accountId;
        private final String symbol;

        SymbolAccount(String accountId, String symbol){
            this.accountId = accountId;
            this.symbol = symbol;
        }

        public String getAccountId() {
            return accountId;
        }

        public String getSymbol() {
            return symbol;
        }

    }

}
