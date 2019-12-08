package com.apps.potok.exchange.core;

import com.apps.potok.exchange.account.Account;
import com.apps.potok.soketio.model.order.NewOrder;
import org.springframework.stereotype.Service;

@Service
public class ExchangeApplication {

    private final Exchange exchange;
    private final OrderManager orderManager;

    public ExchangeApplication(Exchange exchange, OrderManager orderManager) {
        this.exchange = exchange;
        this.orderManager = orderManager;
    }

    public Order manageNew(NewOrder newOrder, Account account) {
        Order order = orderManager.manageNew(newOrder, account);
        if(order != null){
            exchange.fireOrder(order);
        }
        return order;
    }

}
