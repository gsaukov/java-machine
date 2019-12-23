package com.apps.potok.exchange.mkdata;

import com.apps.potok.exchange.account.Account;
import com.apps.potok.exchange.core.AbstractExchangeServer;
import com.apps.potok.exchange.core.Exchange;
import com.apps.potok.exchange.core.ExchangeApplication;
import com.apps.potok.exchange.core.Order;
import com.apps.potok.exchange.core.Position;
import com.apps.potok.exchange.core.SymbolContainer;
import com.apps.potok.exchange.core.Route;
import com.apps.potok.exchange.account.AccountManager;
import com.apps.potok.soketio.model.order.NewOrder;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static com.apps.potok.exchange.core.Route.BUY;
import static com.apps.potok.exchange.core.Route.SELL;

@Service
public class OrderCreatorServer extends AbstractExchangeServer {

    private final ExchangeApplication exchangeApplication;
    private final SymbolContainer symbolContainer;
    private final AccountManager accountManager;

    public OrderCreatorServer(ExchangeApplication exchangeApplication, SymbolContainer symbolContainer, AccountManager accountManager) {
        super.setName("OrderCreatorThread");
        this.exchangeApplication = exchangeApplication;
        this.symbolContainer = symbolContainer;
        this.accountManager = accountManager;
    }

    @Override
    public void runExchangeServer() {
        Account account = randomAccount();
        NewOrder newOrder = randomOrder(account);
        if(newOrder!=null){
            exchangeApplication.manageNew(newOrder, account);
        }
    }

    @Override
    public void speedControl() {
        exchangeSpeed.orderServerSpeedControl();
    }

    private Account randomAccount() {
        String accountId = accountManager.getAllAccountIds().get(RandomUtils.nextInt(0, accountManager.getAllAccountIds().size()));
        return accountManager.getAccount(accountId);
    }

    private NewOrder randomOrder(Account account) {
        if(BUY.equals(getRoute())){
            return randomBuyOrder();
        } else {
            return randomSellOrder(account);
        }
    }

    private NewOrder randomBuyOrder() {
        String symbol = symbolContainer.get(RandomUtils.nextInt(0, symbolContainer.getSymbols().size()));
        Integer val = getDynamicPrice(symbol);
        Integer volume = RandomUtils.nextInt(1, 100) * 10;
        return toNewOrder(symbol, BUY.name(), val, volume);
    }

    private NewOrder randomSellOrder(Account account) {
        List<Position> positions = new ArrayList<>(account.getPositions());
        NewOrder newOrder = null;
        int retryCount = 0;
        while(newOrder == null && retryCount < 3) {
            retryCount++;
            Position position = positions.get((RandomUtils.nextInt(0, positions.size())));
            String symbol = position.getSymbol();
            Long availableVolume = account.getExistingPositivePositionVolume(symbol) - account.getExistingSellOrderVolume(symbol);
            if(availableVolume <= 0) {
                continue;
            }
            Long volume = Math.min(availableVolume, RandomUtils.nextInt(1, 100) * 10);
            Integer val = getDynamicPrice(symbol);
            newOrder = toNewOrder(symbol, SELL.name(), val, volume.intValue());
        }
        return newOrder;
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

    private NewOrder toNewOrder(String symbol, String route, Integer val, Integer volume){
        NewOrder newOrder = new NewOrder();
        newOrder.setSymbol(symbol);
        newOrder.setRoute(route);
        newOrder.setVal(val);
        newOrder.setVolume(volume);
        return newOrder;
    }
}
