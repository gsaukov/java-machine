package com.apps.potok.soketio.listeners;

import com.apps.potok.exchange.core.Exchange;
import com.apps.potok.exchange.core.Order;
import com.apps.potok.exchange.core.OrderManager;
import com.apps.potok.exchange.core.SymbolContainer;
import com.apps.potok.exchange.mkdata.Route;
import com.apps.potok.soketio.model.order.NewOrder;
import com.apps.potok.soketio.server.Account;
import com.apps.potok.soketio.server.AccountManager;
import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.DataListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.apps.potok.exchange.mkdata.Route.BUY;
import static com.apps.potok.soketio.config.SessionUtil.getAccountId;

@Component
public class NewOrderListener implements DataListener<NewOrder> {

    private final int RISK_FACTOR = 10;

    @Autowired
    private Exchange exchange;

    @Autowired
    private OrderManager orderManager;

    @Autowired
    private AccountManager accountManager;

    @Autowired
    private SymbolContainer symbolContainer;

    @Override
    public void onData(SocketIOClient client, NewOrder newOrder, AckRequest ackSender) throws Exception {
        String accountId = getAccountId(client);
        Account account = accountManager.getAccount(accountId);


        Order order = orderManager(newOrder, account);
        if(order!=null){
            orderManager.addOrder(order);
            newOrder.setUuid(order.getUuid().toString());
            client.sendEvent("orderConfirm", newOrder);
            exchange.fireOrder(order);
        } else {
            client.sendEvent("message", "order failed", newOrder );
        }
    }

    //maybe send it to order manager?
    private Order orderManager(NewOrder newOrder, Account account){
        Route route = getRoute(newOrder);
        if(BUY.equals(route)){
            return orderBalanceProcessor(newOrder, account, route);
        } else {
            return new Order(newOrder.getSymbol(), account.getAccountId(), route, newOrder.getVal(), newOrder.getVolume());
        }
    }

    private Order orderBalanceProcessor(NewOrder newOrder, Account account, Route route){
        long predictedAmount = newOrder.getVolume() * symbolContainer.getQuote(newOrder.getSymbol());
        long balanceRisk = predictedAmount + predictedAmount/RISK_FACTOR;
        long balanceChange = newOrder.getVolume() * newOrder.getVal();
        boolean success = account.doNegativeOrderBalance(balanceRisk, balanceChange);
        if(success){
            return new Order(newOrder.getSymbol(), account.getAccountId(), route, newOrder.getVal(), newOrder.getVolume());
        } else {
            return null;
        }
    }

    private Route getRoute(NewOrder newOrder) {
        return Route.valueOf(newOrder.getRoute());
    }

}
