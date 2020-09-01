package com.apps.potok.exchange.notifiers;

import com.apps.potok.exchange.core.AbstractExchangeServer;
import com.apps.potok.exchange.query.QuoteManager;
import com.apps.potok.soketio.model.quote.QuoteResponse;
import com.corundumstudio.socketio.SocketIOServer;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentLinkedDeque;

@Service
public class QuoteNotifier extends AbstractExchangeServer {

    private final QuoteManager quoteManager;
    private final SocketIOServer server;
    private final ConcurrentLinkedDeque<String> eventQueue = new ConcurrentLinkedDeque<>();
//    private final BlockingDeque<String> eventQueue = new LinkedBlockingDeque<>();

    public QuoteNotifier(QuoteManager quoteManager, SocketIOServer server){
        super.setName("QuoteNotifierThread");
        this.quoteManager = quoteManager;
        this.server = server;
    }

    @Override
    public void runExchangeServer() {
        String symbol = eventQueue.poll();
        if(symbol != null){
            QuoteResponse response = quoteManager.searchAllOffers(symbol);
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
