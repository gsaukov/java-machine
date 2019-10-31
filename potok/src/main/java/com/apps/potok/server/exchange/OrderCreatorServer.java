package com.apps.potok.server.exchange;

import com.apps.potok.server.mkdata.Route;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.apps.potok.server.mkdata.Route.BUY;
import static com.apps.potok.server.mkdata.Route.SELL;

@Service
public class OrderCreatorServer extends Thread {

    private final Exchange exchange;
    private final SymbolContainer symbolContainer;
    private final BidContainer bidContainer;
    private final AskContainer askContainer;

    public OrderCreatorServer(Exchange exchange, BidContainer bidContainer, AskContainer askContainer, SymbolContainer symbolContainer) {
        super.setDaemon(true);
        super.setName("OrderCreatorThread");

        this.exchange = exchange;
        this.symbolContainer = symbolContainer;
        this.bidContainer = bidContainer;
        this.askContainer = askContainer;
    }

    @Override
    public void run() {
        while (true){
            exchange.fireOrder(randomOrder());
            try {
                Thread.sleep(RandomUtils.nextInt(0,30));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void insertBidOrder(Order order) {
        bidContainer.insertBid(order.getSymbol(), order.getVal(), order.getAccount());
    }

    public void insertAskOrder(Order order) {
        askContainer.insertAsk(order.getSymbol(), order.getVal(), order.getAccount());
    }

    private Order randomOrder(){
        List<String> symbols = symbolContainer.getSymbols();
        String symbol = symbols.get(RandomUtils.nextInt(0, symbols.size()));
        Route route = getRoute();
        Integer val = getVal(symbol, route);
        return new Order(symbol, RandomStringUtils.randomAlphabetic(4), route, val);
    }

    private Route getRoute() {
        if(RandomUtils.nextBoolean()){
            return BUY;
        } else {
            return SELL;
        }
    }

    private Integer getVal(String symbol, Route route) {
        Integer val = symbolContainer.getQuote(symbol);
        if(!BUY.equals(route)){
            return RandomUtils.nextInt(1, val);
        } else {
            return RandomUtils.nextInt(val - 1, 100);
        }
    }
}
