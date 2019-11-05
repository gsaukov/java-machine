package com.apps.potok.server.eventhandlers;

import com.apps.potok.server.query.QueryServer;
import com.apps.potok.soketio.model.quote.QuoteResponse;
import com.corundumstudio.socketio.SocketIOServer;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentLinkedQueue;

@Service
public class EventNotifierServerV2 extends Thread  {

    private final ConcurrentLinkedQueue<String> eventQueue;

    private QueryServer queryServer;
    private SocketIOServer server;

    public EventNotifierServerV2(QueryServer queryServer, SocketIOServer server){
        super.setDaemon(true);
        super.setName("EventNotifierThread");
        this.queryServer = queryServer;
        this.server = server;
        eventQueue = new ConcurrentLinkedQueue<>();
    }

    @Override
    public void run() {
        while (true){
            String symbol = eventQueue.poll();
            if(symbol != null){
                QuoteResponse response = queryServer.searchAllOffers(symbol);
                server.getRoomOperations(symbol).sendEvent("quoteResponse", response);
            }
        }
    }

    public void pushEvent (String symbol) {
        if (!eventQueue.contains(symbol)){
            eventQueue.offer(symbol);
        }
    }
}
