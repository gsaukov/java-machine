package com.apps.potok.exchange.eventhandlers;

import com.apps.potok.exchange.core.AbstractExchangeServer;
import com.apps.potok.exchange.core.Order;
import com.apps.potok.exchange.core.OrderManager;
import com.apps.potok.soketio.model.execution.Execution;
import com.apps.potok.soketio.server.Account;
import com.apps.potok.soketio.server.AccountManager;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.LinkedBlockingDeque;

@Service
public class ExecutionNotifierServer extends AbstractExchangeServer {

    private SocketIOServer server;
    private AccountManager accountManager;
    private final OrderManager orderManager;
    private final ConcurrentLinkedDeque<Execution> eventQueue = new ConcurrentLinkedDeque<>();
//    private final BlockingDeque<Execution> eventQueue = new LinkedBlockingDeque<>();

    public ExecutionNotifierServer(SocketIOServer server, AccountManager accountManager, OrderManager orderManager){
        super.setName("ExecutionNotifierThread");
        this.server = server;
        this.accountManager = accountManager;
        this.orderManager = orderManager;
    }

    @Override
    public void runExchangeServer() {
        Execution execution = eventQueue.poll();
        if(execution != null){
            Order executedOrder = orderManager.manageExecution(execution);
            notifyClients(getAccount(execution), execution);
        } else {
            exchangeSpeed.notifierSpeedControl();
        }
    }

    @Override
    public void speedControl() {}

    private void notifyClients(Account account, Execution execution) {
        List<UUID> clients = account.getClientUuids();
        if(clients != null && !clients.isEmpty()){
            for(UUID clientUuid : clients){
                SocketIOClient client = server.getClient(clientUuid);
                if (client != null){
                    client.sendEvent("execution", execution);
                }
            }
        }
    }

    public void pushFill(Order order, Integer fillPrice) {
        Execution execution = new Execution(order, fillPrice, order.getVolume(), true);
        eventQueue.offer(execution);
    }

    public void pushPartFill(Order order, Integer fillPrice, Integer quantity) {
        Execution execution = new Execution(order, fillPrice, quantity, false);
        eventQueue.offer(execution);
    }

    private Account getAccount(Execution execution) {
        return accountManager.getAccount(execution.getAccountId());
    }
}
