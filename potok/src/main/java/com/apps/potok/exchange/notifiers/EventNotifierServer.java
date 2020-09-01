package com.apps.potok.exchange.notifiers;

import com.apps.potok.exchange.query.QuoteManager;
import com.apps.potok.soketio.listeners.QuoteSubscribers;
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
    private QuoteManager quoteManager;
    private SocketIOServer server;

    public EventNotifierServer(QuoteSubscribers quoteSubscribers, QuoteManager quoteManager, SocketIOServer server){
        super.setName("EventNotifierThread");
        this.quoteSubscribers = quoteSubscribers;
        this.quoteManager = quoteManager;
        this.server = server;
        eventQueue = new ConcurrentLinkedDeque<>();
    }

    @Override
    public void run() {
        while (true){
            String symbol = eventQueue.poll();
            if(symbol != null){
                QuoteResponse response = quoteManager.searchAllOffers(symbol);
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
