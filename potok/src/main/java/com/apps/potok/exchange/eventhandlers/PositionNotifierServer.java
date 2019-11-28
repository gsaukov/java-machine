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
    private final ConcurrentLinkedDeque<Position> eventQueue = new ConcurrentLinkedDeque<>();

    public PositionNotifierServer(SocketIOServer server, AccountManager accountManager) {
        super.setName("PositionNotifierServer");
        this.server = server;
        this.accountManager = accountManager;
    }

    @Override
    public void runExchangeServer() {
        Position position = eventQueue.poll();
        if(position != null){
            notifyClients(position);
        }
    }

    @Override
    public void speedControl() {}

    private void notifyClients(Position position) {
        Account account = getAccount(position);
        List<UUID> clients = account.getClientUuids();
        if(clients != null && !clients.isEmpty()){
            PositionNotification notification = new PositionNotification(position);
            for(UUID clientUuid : clients){
                SocketIOClient client = server.getClient(clientUuid);
                if (client != null){
                    client.sendEvent("positionNotification", notification);
                }
            }
        }
    }

    public void pushPosition(Position position) {
        eventQueue.offer(position);
    }

    private Account getAccount(Position position) {
        return accountManager.getAccount(position.getAccountId());
    }



}
