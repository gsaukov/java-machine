package com.apps.potok.soketio.listeners;

import com.apps.potok.soketio.model.LogLine;
import com.apps.potok.soketio.model.order.CancelOrder;
import com.apps.potok.soketio.model.order.NewOrder;
import com.apps.potok.soketio.model.quote.QuoteRequest;
import com.corundumstudio.socketio.SocketIOServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Configuration
public class ListenerConfigurator {

    @Autowired
    private SocketIOServer server;

    @Autowired
    private ChatMessageListener chatMessageListener;

    @Autowired
    private QuoteSubscriptionListener quoteMessageListener;

    @Autowired
    private NewOrderListener newOrderListener;

    @Autowired
    private CancelOrderListener cancelOrderListener;

    @PostConstruct
    public void webSocketServer() throws IOException {
        server.addEventListener("message", LogLine.class, chatMessageListener);
        server.addEventListener("quoteRequest", QuoteRequest.class, quoteMessageListener);
        server.addEventListener("newOrder", NewOrder.class, newOrderListener);
        server.addEventListener("cancelOrder", CancelOrder.class, cancelOrderListener);
    }
}
