package com.apps.potok.exchange.eventhandlers;

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
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class ExecutionNotifierServer extends Thread  {

    private SocketIOServer server;
    private AccountManager accountManager;
    private final OrderManager orderManager;
    private final AtomicBoolean running = new AtomicBoolean(true);
    private final ConcurrentLinkedDeque<Execution> eventQueue = new ConcurrentLinkedDeque<>();

    public ExecutionNotifierServer(SocketIOServer server, AccountManager accountManager, OrderManager orderManager){
        super.setDaemon(true);
        super.setName("ExecutionNotifierThread");
        this.server = server;
        this.accountManager = accountManager;
        this.orderManager = orderManager;
    }

    @Override
    public void run() {
        while (running.get()){
            Execution execution = eventQueue.poll();
            if(execution != null){
                Order executedOrder = orderManager.manageExecution(execution);
                notifyClients(getAccount(execution), execution);
            }
        }
    }

    public void stopQuoteNotifier(){
        running.getAndSet(false);
    }

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
        if (accountManager.containsAccount(order.getAccount())){
            Execution execution = new Execution(order.getUuid(), order.getAccount(), fillPrice, order.getVolume(), true);
            eventQueue.offer(execution);
        }
    }

    public void pushPartFill(Order order, Integer fillPrice, Integer quantity) {
        if (accountManager.containsAccount(order.getAccount())){
            Execution execution = new Execution(order.getUuid(), order.getAccount(), fillPrice, quantity, false);
            eventQueue.offer(execution);
        }
    }

    private Account getAccount(Execution execution) {
        return accountManager.getAccount(execution.getAccountId());
    }
}
