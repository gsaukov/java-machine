package com.apps.potok.exchange.core;

import com.apps.potok.exchange.mkdata.Route;
import com.apps.potok.exchange.account.AccountManager;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static com.apps.potok.exchange.mkdata.Route.BUY;
import static com.apps.potok.exchange.mkdata.Route.SELL;

@Service
public class OrderCreatorServer extends AbstractExchangeServer {

    private final Exchange exchange;
    private final SymbolContainer symbolContainer;
    private final AccountManager accountManager;
    private final List<String> accounts = new ArrayList<>();

    public OrderCreatorServer(Exchange exchange, SymbolContainer symbolContainer, AccountManager accountManager) {
        super.setName("OrderCreatorThread");
        this.exchange = exchange;
        this.symbolContainer = symbolContainer;
        this.accountManager = accountManager;
    }

    @Override
    public void runExchangeServer() {
        accounts.addAll(accountManager.getAllAccountIds());
        exchange.fireOrder(randomOrder());
    }

    @Override
    public void speedControl() {
        exchangeSpeed.orderServerSpeedControl();
    }

    private Order randomOrder(){
        String symbol = symbolContainer.get(RandomUtils.nextInt(0, symbolContainer.getSymbols().size()));
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
