package com.apps.potok.exchange.notifiers;

import com.apps.potok.exchange.core.AbstractExchangeServer;
import com.apps.potok.exchange.core.ExecutionManager;
import com.apps.potok.exchange.core.Order;
import com.apps.potok.exchange.core.OrderManager;
import com.apps.potok.kafka.producer.ExecutionMessageProducer;
import com.apps.potok.soketio.model.execution.Execution;
import com.apps.potok.exchange.account.Account;
import com.apps.potok.exchange.account.AccountManager;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedDeque;

@Service
public class ExecutionNotifier extends AbstractExchangeServer {

    private SocketIOServer server;
    private ExecutionMessageProducer executionMessageProducer;
    private AccountManager accountManager;
    private final ExecutionManager executionManager; //
    private final ConcurrentLinkedDeque<Execution> eventQueue = new ConcurrentLinkedDeque<>();
//    private final BlockingDeque<Execution> eventQueue = new LinkedBlockingDeque<>();

    public ExecutionNotifier(SocketIOServer server, AccountManager accountManager, ExecutionManager executionManager,
                             ExecutionMessageProducer executionMessageProducer){
        super.setName("ExecutionNotifierThread");
        this.server = server;
        this.executionMessageProducer = executionMessageProducer;
        this.accountManager = accountManager;
        this.executionManager = executionManager;
    }

    @Override
    public void runExchangeServer() {
        Execution execution = eventQueue.poll();
        if(execution != null){
            Order executedOrder = executionManager.manageExecution(execution);
            notifyClients(getAccount(execution), execution);
            executionMessageProducer.sendExecutionMessage(execution);
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

    public void pushExecution(Execution execution) {
        eventQueue.offer(execution);
    }

    private Account getAccount(Execution execution) {
        return accountManager.getAccount(execution.getAccountId());
    }
}
