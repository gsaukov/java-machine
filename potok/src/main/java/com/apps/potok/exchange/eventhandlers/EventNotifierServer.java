package com.apps.potok.exchange.eventhandlers;

import com.apps.potok.exchange.query.QueryServer;
import com.apps.potok.soketio.model.quote.QuoteResponse;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedDeque;

//@Service
//Use broadcasting EventNotifierServerV2
@Deprecated
public class EventNotifierServer extends Thread  {

    private final ConcurrentLinkedDeque<String> eventQueue;

    private QuoteSubscribers quoteSubscribers;
    private QueryServer queryServer;
    private SocketIOServer server;

    public EventNotifierServer(QuoteSubscribers quoteSubscribers, QueryServer queryServer, SocketIOServer server){
        super.setName("EventNotifierThread");
        this.quoteSubscribers = quoteSubscribers;
        this.queryServer = queryServer;
        this.server = server;
        eventQueue = new ConcurrentLinkedDeque<>();
    }

    @Override
    public void run() {
        while (true){
            String symbol = eventQueue.poll();
            if(symbol != null){
                QuoteResponse response = queryServer.searchAllOffers(symbol);
                Map<UUID, String> subscribers = quoteSubscribers.getSubscribers(symbol);
                if(subscribers != null) {
                    subscribers.forEach( (key, value) -> {
                        SocketIOClient client = server.getClient(key);
                        if(client != null){
                            server.getClient(key).sendEvent("quoteResponse", response);
                        } else {
                            quoteSubscribers.removeSubscriber(key);
                        }

                    });
                }
            }
        }
    }

    public void pushEvent (String symbol){
        if (!eventQueue.contains(symbol)){
            eventQueue.offer(symbol);
        }
    }
}
