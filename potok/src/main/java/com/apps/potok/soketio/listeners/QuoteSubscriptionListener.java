package com.apps.potok.soketio.listeners;

import com.apps.potok.exchange.query.QuoteManager;
import com.apps.potok.soketio.model.quote.QuoteRequest;
import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.DataListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class QuoteSubscriptionListener implements DataListener<QuoteRequest> {

    @Autowired
    private QuoteSubscribersV2 quoteSubscribers;

    @Autowired
    private QuoteManager quoteManager;

    @Autowired
    private SocketIOServer server;

    @Override
    public void onData(SocketIOClient client, QuoteRequest data, AckRequest ackRequest) {
        quoteSubscribers.removeSubscriber(client.getSessionId());
        quoteSubscribers.addSubscriber(data.getSymbol(), client.getSessionId());
        server.getClient(client.getSessionId()).sendEvent("quoteResponse", quoteManager.searchAllOffers(data.getSymbol()));
    }
}
