package com.apps.potok.exchange.core;

import com.apps.potok.exchange.mkdata.Route;
import com.apps.potok.soketio.server.AccountManager;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.apps.potok.exchange.mkdata.Route.BUY;
import static com.apps.potok.exchange.mkdata.Route.SELL;

@Service
public class OrderCreatorServer extends Thread {

    private final Exchange exchange;
    private final OrderManager orderManager;
    private final SymbolContainer symbolContainer;
    private final AtomicBoolean running = new AtomicBoolean(true);
    private final List<String> symbols;
    private final List<String> accounts;

    public OrderCreatorServer(Exchange exchange, SymbolContainer symbolContainer, AccountManager accountContainer, OrderManager orderManager) {
        super.setDaemon(true);
        super.setName("OrderCreatorThread");

        this.exchange = exchange;
        this.orderManager = orderManager;
        this.symbolContainer = symbolContainer;

        this.symbols = symbolContainer.getSymbols();
        this.accounts = accountContainer.getAllAccountIds();
    }

    @Override
    public void run() {
        while(running.get()){
            Order order = randomOrder();
            orderManager.addOrder(order);
            exchange.fireOrder(order);
        }
    }

    public void stopOrderCreator(){
        running.getAndSet(false);
    }

    private Order randomOrder(){
        String symbol = symbols.get(RandomUtils.nextInt(0, symbols.size()));
        String accountId = accounts.get(RandomUtils.nextInt(0, accounts.size()));
        Route route = getRoute();
        Integer val = getDynamicPrice(symbol);
        Integer volume = RandomUtils.nextInt(0, 100) * 10;
        return new Order(symbol, accountId, route, val, volume);
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
