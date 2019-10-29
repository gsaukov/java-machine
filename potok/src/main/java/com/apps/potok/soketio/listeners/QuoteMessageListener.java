package com.apps.potok.soketio.listeners;

import com.apps.potok.server.eventhandlers.QuoteSubscribers;
import com.apps.potok.server.query.QueryServer;
import com.apps.potok.soketio.model.quote.QuoteRequest;
import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.protocol.Packet;
import com.corundumstudio.socketio.protocol.PacketType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class QuoteMessageListener implements DataListener<QuoteRequest> {

    @Autowired
    private QuoteSubscribers quoteSubscribers;

    @Autowired
    private QueryServer queryServer;

    @Autowired
    private SocketIOServer server;

    @Override
    public void onData(SocketIOClient client, QuoteRequest data, AckRequest ackRequest) {
        quoteSubscribers.removeSubscriber(data.getSymbol(), client.getSessionId().toString());
        quoteSubscribers.addSubscriber(data.getSymbol(), client.getSessionId().toString());
        server.getClient(client.getSessionId()).sendEvent("quoteResponse", queryServer.searchAllOffers(data.getSymbol()));
    }
}