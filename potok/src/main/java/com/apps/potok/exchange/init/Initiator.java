package com.apps.potok.exchange.init;

import com.apps.potok.exchange.core.AskComparator;
import com.apps.potok.exchange.core.Order;
import com.apps.potok.exchange.core.SymbolContainer;
import com.apps.potok.exchange.mkdata.Route;
import com.apps.potok.soketio.server.AccountContainer;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicLong;

import static com.apps.potok.exchange.mkdata.Route.BUY;

@Service
public class Initiator {

    private SymbolContainer symbolContainer;
    private AccountContainer accountContainer;

    private AtomicLong askInit = new AtomicLong(0l);
    private AtomicLong bidInit = new AtomicLong(0l);

    public Initiator(SymbolContainer symbolContainer, AccountContainer accountContainer) {
        this.symbolContainer = symbolContainer;
        this.accountContainer = accountContainer;
    }

    public void initiateContainer (int size, ConcurrentHashMap<String, ConcurrentSkipListMap<Integer, ConcurrentLinkedDeque<Order>>> orderContainer, Route route){
        for(Order order : getOrders(size, route)){
            insertOrder(orderContainer, order, route);
        }
    }

    private void insertOrder(ConcurrentHashMap<String, ConcurrentSkipListMap<Integer, ConcurrentLinkedDeque<Order>>> allOrderContainer, Order order, Route route) {
        ConcurrentSkipListMap<Integer, ConcurrentLinkedDeque<Order>> symbolOrderContainer = allOrderContainer.get(order.getSymbol());

        if(symbolOrderContainer == null){
            if(BUY.equals(route)){
                symbolOrderContainer = new ConcurrentSkipListMap(new AskComparator());
            } else {
                symbolOrderContainer = new ConcurrentSkipListMap();
            }
            allOrderContainer.put(order.getSymbol(), symbolOrderContainer);
        }

        insertPrice(symbolOrderContainer, order);
    }

    private void insertPrice(ConcurrentSkipListMap<Integer, ConcurrentLinkedDeque<Order>> symbolOrderContainer, Order order) {
        ConcurrentLinkedDeque<Order> customerContainer = symbolOrderContainer.get(order.getVal());

        if(customerContainer == null){
            customerContainer = new ConcurrentLinkedDeque();
            symbolOrderContainer.put(order.getVal(), customerContainer);
        }

        customerContainer.add(order);

    }

    private List<Order> getOrders(int size, Route route) {
        List<String> symbols = symbolContainer.getSymbols();
        List<String> accounts = accountContainer.getAllAccountIds();
        List<Order> orders = new ArrayList<>();
        for(int i = 0 ; i < size; i++){
            String customer = accounts.get(RandomUtils.nextInt(0, accounts.size()));
            String symbol = symbols.get(RandomUtils.nextInt(0, symbols.size()));
            Integer val = getVal(symbol, route);
            Integer volume = RandomUtils.nextInt(0, 100) * 10;
            Order order = new Order(symbol, customer, route, val, volume);
            count(order);
            orders.add(order);
        }
        return orders;
    }

    private Integer getVal(String symbol, Route route) {
        Integer val = symbolContainer.getQuote(symbol);
        if(BUY.equals(route)){
            return RandomUtils.nextInt(1, val);
        } else {
            return RandomUtils.nextInt(val - 1, 100);
        }
    }

    private void count(Order order) {
        if(BUY.equals(order.getRoute())){
            askInit.getAndAdd(order.getVolume());
        } else {
            bidInit.getAndAdd(order.getVolume());
        }
    }

    public long getAskInit() {
        return askInit.get();
    }

    public long getBidInit() {
        return bidInit.get();
    }
}
