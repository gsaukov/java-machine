package com.apps.potok.server.exchange;

import com.apps.potok.server.mkdata.Route;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

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
        int i = 0;
        while(i<10000000){
            exchange.fireOrder(randomOrder());
//            try {
////                Thread.sleep(RandomUtils.nextInt(0,30));
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
            i++;
        }
        System.out.println("done push Order Server");
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
        Integer val = getVal(symbol);
        return new Order(symbol, RandomStringUtils.randomAlphabetic(4), route, val);
    }

    private Route getRoute() {
        if(RandomUtils.nextBoolean()){
            return BUY;
        } else {
            return SELL;
        }
    }

    private Integer getVal(String symbol) {
        Integer val = symbolContainer.getQuote(symbol);
        return getDynamicPrice(val);
    }

    private Integer getDynamicPrice (Integer val) {
        ThreadLocalRandom r = ThreadLocalRandom.current();
        int coefficient = Math.toIntExact(Math.round(val * 0.1d + 0.5 + r.nextGaussian()));
        if(RandomUtils.nextBoolean()){
            return val - coefficient;
        } else {
            return val + coefficient;
        }
    }
}
