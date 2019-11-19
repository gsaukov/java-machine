package com.apps.potok.soketio.listeners;

import com.apps.potok.exchange.core.Exchange;
import com.apps.potok.exchange.core.Order;
import com.apps.potok.exchange.core.OrderManager;
import com.apps.potok.exchange.mkdata.Route;
import com.apps.potok.soketio.model.order.NewOrder;
import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.DataListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.apps.potok.soketio.config.SessionUtil.getAccountId;

@Component
public class NewOrderListener implements DataListener<NewOrder> {

    @Autowired
    private Exchange exchange;

    @Autowired
    private OrderManager orderManager;

    @Override
    public void onData(SocketIOClient client, NewOrder newOrder, AckRequest ackSender) throws Exception {
        String accountId = getAccountId(client);
        Route route = getRoute(newOrder);
        Order order = new Order(newOrder.getSymbol(), accountId, route, newOrder.getVal(), newOrder.getVolume());
        orderManager.addOrder(order);
        newOrder.setUuid(order.getUuid().toString());
        client.sendEvent("orderConfirm", newOrder);
        exchange.fireOrder(order);
    }

    private Route getRoute(NewOrder newOrder) {
        return Route.valueOf(newOrder.getRoute());
    }

}
