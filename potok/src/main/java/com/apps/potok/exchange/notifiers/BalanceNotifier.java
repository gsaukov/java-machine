package com.apps.potok.exchange.notifiers;

import com.apps.potok.exchange.core.AbstractExchangeServer;
import com.apps.potok.exchange.account.Account;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedDeque;

@Service
public class BalanceNotifier extends AbstractExchangeServer {
    private SocketIOServer server;
    private final ConcurrentLinkedDeque<Account> eventQueue = new ConcurrentLinkedDeque<>();
//    private final BlockingDeque<Account> eventQueue = new LinkedBlockingDeque<>();

    public BalanceNotifier(SocketIOServer server) {
        super.setName("BalanceNotifier");
        this.server = server;
    }

    @Override
    public void runExchangeServer() {
        Account account = eventQueue.poll();
        if(account != null){
            notifyClients(account);
        } else {
            exchangeSpeed.notifierSpeedControl();
        }
    }

    @Override
    public void speedControl() {}

    private void notifyClients(Account account) {
        long balance = account.getBalance();
        List<UUID> clients = account.getClientUuids();
        if(clients != null && !clients.isEmpty()){
            for(UUID clientUuid : clients){
                SocketIOClient client = server.getClient(clientUuid);
                if (client != null){
                    client.sendEvent("balance", balance);
                }
            }
        }
    }

    public void pushBalance(Account account) {
        eventQueue.offer(account);
    }

}
