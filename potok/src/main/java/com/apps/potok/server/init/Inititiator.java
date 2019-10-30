package com.apps.potok.server.init;


import com.apps.potok.server.exchange.Order;
import com.apps.potok.server.exchange.SymbolContainer;
import com.apps.potok.server.mkdata.Route;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.apps.potok.server.mkdata.Route.BUY;

@Service
public class Inititiator {

    private SymbolContainer symbolContainer;

    public Inititiator(SymbolContainer symbolContainer) {
        this.symbolContainer = symbolContainer;
    }

    public void initiateContainer (int size, HashMap<String, ConcurrentSkipListMap<Integer, CopyOnWriteArrayList<String>>> orderContainer, Route route){
        for(Order order : getOrders(size, route)){
            insertOrder(orderContainer, order);
        }
    }

    private void insertOrder(HashMap<String, ConcurrentSkipListMap<Integer, CopyOnWriteArrayList<String>>> allOrderContainer, Order order) {
        ConcurrentSkipListMap<Integer, CopyOnWriteArrayList<String>> symbolOrderContainer = allOrderContainer.get(order.getSymbol());

        if(symbolOrderContainer == null){
            symbolOrderContainer = new ConcurrentSkipListMap();
            allOrderContainer.put(order.getSymbol(), symbolOrderContainer);
        }

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

    private List<Order> getOrders(int size, Route route) {
        List<String> symbols = symbolContainer.getSymbols();
        List<String> customers = getCustomers(size);
        List<Order> orders = new ArrayList<>();
        for(int i = 0 ; i < size; i++){
            String customer = customers.get(RandomUtils.nextInt(0, customers.size()));
            String symbol = symbols.get(RandomUtils.nextInt(0, symbols.size()));
            Integer val = getVal(symbol, route);
            System.out.println(route + " " +val);
            orders.add(new Order(symbol, customer, route, val));
        }
        return orders;
    }

    private List<String> getCustomers(int size) {
        List<String> customers = new ArrayList<>();

        for(int i = 0 ; i < size/5 ; i++){
            customers.add(RandomStringUtils.randomAlphabetic(3) + " " + RandomStringUtils.randomAlphabetic(5));
        }

        return customers;
    }

    private Integer getVal(String symbol, Route route) {
        Integer val = symbolContainer.getQuote(symbol);
        if(BUY.equals(route)){
            return RandomUtils.nextInt(1, val);
        } else {
            return RandomUtils.nextInt(val - 1, 100);
        }
    }
}
