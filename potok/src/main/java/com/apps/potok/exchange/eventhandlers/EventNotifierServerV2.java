package com.apps.potok.exchange.eventhandlers;

import com.apps.potok.exchange.query.QueryServer;
import com.apps.potok.soketio.model.quote.QuoteResponse;
import com.corundumstudio.socketio.SocketIOServer;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class EventNotifierServerV2 extends Thread  {

    private QueryServer queryServer;
    private SocketIOServer server;
    private final AtomicBoolean running = new AtomicBoolean(true);
    private final ConcurrentLinkedDeque<String> eventQueue = new ConcurrentLinkedDeque<>();

    public EventNotifierServerV2(QueryServer queryServer, SocketIOServer server){
        super.setDaemon(true);
        super.setName("EventNotifierThread");
        this.queryServer = queryServer;
        this.server = server;
    }

    @Override
    public void run() {
        while (running.get()){
            String symbol = eventQueue.poll();
            if(symbol != null){
                QuoteResponse response = queryServer.searchAllOffers(symbol);
                server.getRoomOperations(symbol).sendEvent("quoteResponse", response);
            }
        }
    }

    public void stopEventNotifier (){
        running.getAndSet(false);
    }

    public void pushEvent (String symbol) {
        if (!eventQueue.contains(symbol)){
            eventQueue.offer(symbol);
        }
    }
}
