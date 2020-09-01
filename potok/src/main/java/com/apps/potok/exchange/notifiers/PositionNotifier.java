package com.apps.potok.exchange.notifiers;

import com.apps.potok.exchange.core.AbstractExchangeServer;
import com.apps.potok.exchange.core.Position;
import com.apps.potok.exchange.core.Route;
import com.apps.potok.soketio.model.execution.PositionNotification;
import com.apps.potok.exchange.account.Account;
import com.apps.potok.exchange.account.AccountManager;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedDeque;

import static com.apps.potok.exchange.core.Route.SHORT;

@Service
public class PositionNotifier extends AbstractExchangeServer {

    private AccountManager accountManager;
    private SocketIOServer server;
    private final ConcurrentLinkedDeque<SymbolAccount> eventQueue = new ConcurrentLinkedDeque<>();
//    private final BlockingDeque<SymbolAccount> eventQueue = new LinkedBlockingDeque<>();


    public PositionNotifier(SocketIOServer server, AccountManager accountManager) {
        super.setName("PositionNotifier");
        this.server = server;
        this.accountManager = accountManager;
    }

    @Override
    public void runExchangeServer() {
        SymbolAccount symbolAccount = eventQueue.poll();
        if(symbolAccount != null){
            notifyClients(symbolAccount);
        } else {
            exchangeSpeed.notifierSpeedControl();
        }
    }

    @Override
    public void speedControl() {}

    private void notifyClients(SymbolAccount symbolAccount) {
        Account account = getAccount(symbolAccount.getAccountId());
        List<UUID> clients = account.getClientUuids();
        if(clients != null && !clients.isEmpty()){
            Position position = getPosition(account, symbolAccount);
            PositionNotification notification = new PositionNotification(position);
            for(UUID clientUuid : clients){
                SocketIOClient client = server.getClient(clientUuid);
                if (client != null){
                    client.sendEvent("positionNotification", notification);
                }
            }
        }
    }

    public void pushPositionNotification(String accountId, String symbol, Route route) {
        eventQueue.offer(new SymbolAccount(accountId, symbol, route));
    }

    private Account getAccount(String accountId) {
        return accountManager.getAccount(accountId);
    }

    private Position getPosition(Account account, SymbolAccount symbolAccount){
        if(SHORT.equals(symbolAccount.getRoute())){
            return account.getShortPosition(symbolAccount.getSymbol());
        } else {
            return account.getPosition(symbolAccount.getSymbol());
        }
    }

    private static class SymbolAccount {

        private final String accountId;
        private final String symbol;
        private final Route route;

        SymbolAccount(String accountId, String symbol, Route route){
            this.accountId = accountId;
            this.symbol = symbol;
            this.route = route;
        }

        public String getAccountId() {
            return accountId;
        }

        public String getSymbol() {
            return symbol;
        }

        public Route getRoute() {
            return route;
        }
    }

}
