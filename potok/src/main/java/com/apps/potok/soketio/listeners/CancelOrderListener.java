package com.apps.potok.soketio.listeners;

import com.apps.potok.exchange.core.CancelOrderManager;
import com.apps.potok.exchange.core.Order;
import com.apps.potok.exchange.notifiers.QuoteNotifier;
import com.apps.potok.soketio.model.order.CancelOrder;
import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.DataListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.apps.potok.soketio.config.SessionUtil.getAccountId;
import static com.apps.potok.soketio.config.SessionUtil.getOrderUuid;

@Service
public class CancelOrderListener implements DataListener<CancelOrder> {

    @Autowired
    private QuoteNotifier quoteNotifier;

    @Autowired
    private CancelOrderManager cancelOrderManager;

    @Override
    public void onData(SocketIOClient client, CancelOrder data, AckRequest ackRequest) {
        String accountId = getAccountId(client);
        UUID uuid = getOrderUuid(data.getUuid());
        Order canceledOrder = cancelOrderManager.cancelOrder(uuid, accountId);
        if (canceledOrder != null) { //order
            quoteNotifier.pushQuote(canceledOrder.getSymbol());
            client.sendEvent("canceledOrder", canceledOrder);
        }
    }

}
