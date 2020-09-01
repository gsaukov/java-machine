package com.apps.potok.soketio.listeners;

import com.apps.potok.exchange.core.Order;
import com.apps.potok.exchange.core.OrderManager;
import com.apps.potok.soketio.model.LogLine;
import com.apps.potok.soketio.model.order.NewOrder;
import com.apps.potok.exchange.account.Account;
import com.apps.potok.exchange.account.AccountManager;
import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.DataListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.apps.potok.soketio.config.SessionUtil.getAccountId;

@Component
public class NewOrderListener implements DataListener<NewOrder> {

    @Autowired
    private OrderManager orderManager;

    @Autowired
    private AccountManager accountManager;

    @Override
    public void onData(SocketIOClient client, NewOrder newOrder, AckRequest ackSender) throws Exception {
        String accountId = getAccountId(client);
        Account account = accountManager.getAccount(accountId);
        Order order = orderManager.manageNew(newOrder, account);
        if(order != null){
            newOrder.setUuid(order.getUuid().toString());
            client.sendEvent("orderConfirm", order);
        } else {
            LogLine logLine = new LogLine();
            logLine.setLine("order failed " + newOrder.getRoute() + " " + newOrder.getSymbol());
            client.sendEvent("message", logLine);
        }
    }

}
