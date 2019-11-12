package com.apps.potok.server.exchange;

import com.apps.potok.server.mkdata.Route;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.apps.potok.server.mkdata.Route.BUY;
import static com.apps.potok.server.mkdata.Route.SELL;

@Service
public class OrderCreatorServer extends Thread {

    private final Exchange exchange;
    private final SymbolContainer symbolContainer;
    private final AtomicBoolean running = new AtomicBoolean(true);

    public OrderCreatorServer(Exchange exchange, SymbolContainer symbolContainer) {
        super.setDaemon(true);
        super.setName("OrderCreatorThread");

        this.exchange = exchange;
        this.symbolContainer = symbolContainer;
    }

    @Override
    public void run() {
        while(running.get()){
            exchange.fireOrder(randomOrder());
        }
    }

    public void stopOrderCreator(){
        running.getAndSet(false);
    }

    private Order randomOrder(){
        List<String> symbols = symbolContainer.getSymbols();
        String symbol = symbols.get(RandomUtils.nextInt(0, symbols.size()));
        Route route = getRoute();
        Integer val = getDynamicPrice(symbol);
        Integer volume = RandomUtils.nextInt(0, 100) * 10;
        return new Order(symbol, RandomStringUtils.randomAlphabetic(4), route, val, volume);
    }

    private Route getRoute() {
        if(RandomUtils.nextBoolean()){
            return BUY;
        } else {
            return SELL;
        }
    }

    private Integer getDynamicPrice (String symbol) {
        Integer val = symbolContainer.getQuote(symbol);
        ThreadLocalRandom r = ThreadLocalRandom.current();
        int coefficient = Math.toIntExact(Math.round(val * 0.1d + 0.5 + r.nextGaussian()));
        if(RandomUtils.nextBoolean()){
            return val - coefficient;
        } else {
            return val + coefficient;
        }
    }
}
