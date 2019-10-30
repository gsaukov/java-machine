package com.apps.potok.server.exchange;

import com.apps.potok.server.mkdata.Route;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class OrderCreatorServer extends Thread {


    private final SymbolContainer symbolContainer;
    private final BidContainer bidContainer;
    private final AskContainer askContainer;

    public OrderCreatorServer(BidContainer bidContainer, AskContainer askContainer, SymbolContainer symbolContainer) {
        super.setDaemon(true);
        super.setName("orderCreatorThread");

        this.symbolContainer = symbolContainer;
        this.bidContainer = bidContainer;
        this.askContainer = askContainer;
    }

    @Override
    public void run() {

            while (true){
                insertBidOrder(randomOrder());
                try {
                    Thread.sleep(RandomUtils.nextInt(0,30));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

    }

    private void insertBidOrder(Order order) {
        ConcurrentSkipListMap<Integer, CopyOnWriteArrayList<String>> symbolOrderContainer = bidContainer.get(order.getSymbol());
        insertPrice(symbolOrderContainer, order);
    }

    private void insertAskOrder(Order order) {
        ConcurrentSkipListMap<Integer, CopyOnWriteArrayList<String>> symbolOrderContainer = askContainer.get(order.getSymbol());
        insertPrice(symbolOrderContainer, order);
    }

    private void insertPrice(ConcurrentSkipListMap<Integer, CopyOnWriteArrayList<String>> symbolOrderContainer, Order order) {
        CopyOnWriteArrayList<String> customerContainer = symbolOrderContainer.get(order.getVal());
        if(customerContainer == null){
            customerContainer = new CopyOnWriteArrayList();
            symbolOrderContainer.put(order.getVal(), customerContainer);
        }
        customerContainer.add(order.getAccount());
    }
    
    private Order randomOrder(){
        List<String> symbols = symbolContainer.getSymbols();
        return new Order(symbols.get(RandomUtils.nextInt(0, symbols.size())),
            "INS " + RandomStringUtils.randomAlphabetic(4), Route.BUY, RandomUtils.nextInt(50, 90));
    }
}
