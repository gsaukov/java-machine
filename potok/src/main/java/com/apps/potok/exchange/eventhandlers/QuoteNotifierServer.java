package com.apps.potok.exchange.eventhandlers;

import com.apps.potok.exchange.core.AbstractExchangeServer;
import com.apps.potok.exchange.query.QueryServer;
import com.apps.potok.soketio.model.quote.QuoteResponse;
import com.corundumstudio.socketio.SocketIOServer;
import org.springframework.stereotype.Service;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.LinkedBlockingDeque;

@Service
public class QuoteNotifierServer extends AbstractExchangeServer {

    private final QueryServer queryServer;
    private final SocketIOServer server;
    private final ConcurrentLinkedDeque<String> eventQueue = new ConcurrentLinkedDeque<>();
//    private final BlockingDeque<String> eventQueue = new LinkedBlockingDeque<>();

    public QuoteNotifierServer(QueryServer queryServer, SocketIOServer server){
        super.setName("QuoteNotifierThread");
        this.queryServer = queryServer;
        this.server = server;
    }

    @Override
    public void runExchangeServer() {
        String symbol = eventQueue.poll();
        if(symbol != null){
            QuoteResponse response = queryServer.searchAllOffers(symbol);
            server.getRoomOperations(symbol).sendEvent("quoteResponse", response);
        } else {
            exchangeSpeed.notifierSpeedControl();
        }
    }

    @Override
    public void speedControl() {}

    public void pushQuote(String symbol) {
        if (!eventQueue.contains(symbol)){
            eventQueue.offer(symbol);
        }
    }
}
