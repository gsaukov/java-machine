package com.apps.potok.exchange.mkdata;

import com.apps.potok.exchange.account.Account;
import com.apps.potok.exchange.core.AbstractExchangeServer;
import com.apps.potok.exchange.core.ExchangeApplication;
import com.apps.potok.exchange.core.Order;
import com.apps.potok.exchange.core.OrderManager;
import com.apps.potok.exchange.core.Position;
import com.apps.potok.exchange.core.SymbolContainer;
import com.apps.potok.exchange.core.Route;
import com.apps.potok.exchange.account.AccountManager;
import com.apps.potok.soketio.model.order.NewOrder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import static com.apps.potok.exchange.core.Route.BUY;
import static com.apps.potok.exchange.core.Route.SELL;
import static com.apps.potok.exchange.core.Route.SHORT;

@Service
public class OrderCreatorServer extends AbstractExchangeServer {

    private final ExchangeApplication exchangeApplication;
    private final SymbolContainer symbolContainer;
    private final AccountManager accountManager;
    private final OrderManager orderManager;
    private final List<UUID> orderToCancel = new ArrayList<>();
    private ThreadLocalRandom r;

    public OrderCreatorServer(ExchangeApplication exchangeApplication, SymbolContainer symbolContainer, AccountManager accountManager, OrderManager orderManager) {
        super.setName("OrderCreatorThread");
        this.exchangeApplication = exchangeApplication;
        this.symbolContainer = symbolContainer;
        this.accountManager = accountManager;
        this.orderManager = orderManager;
    }

    @Override
    public void runExchangeServer() {
        r = ThreadLocalRandom.current();
        Account account = randomAccount();
        NewOrder newOrder = randomOrder(account);
        if(newOrder!=null){
            Order order = exchangeApplication.manageNew(newOrder, account);
            addToCancel(order);
        }
        cancelRandom();
    }

    @Override
    public void speedControl() {
        exchangeSpeed.orderServerSpeedControl();
    }

    private Account randomAccount() {
        String accountId = accountManager.getAllAccountIds().get(r.nextInt(0, accountManager.getAllAccountIds().size()));
        return accountManager.getAccount(accountId);
    }

    private NewOrder randomOrder(Account account) {
        if(BUY.equals(getRoute())){
            return randomBuyOrder();
        } else if (SELL.equals(getRoute())) {
            return randomSellOrder(account);
        } else {
            return randomShortOrder(account);
        }
    }

    private NewOrder randomBuyOrder() {
        String symbol = symbolContainer.get(r.nextInt(0, symbolContainer.getSymbols().size()));
        Integer val = getDynamicPrice(symbol);
        Integer volume = r.nextInt(1, 100) * 10;
        return toNewOrder(symbol, BUY.name(), val, volume);
    }

    private NewOrder randomSellOrder(Account account) {
        List<Position> positions = new ArrayList<>(account.getPositions());
        NewOrder newOrder = null;
        int retryCount = 0;
        while(newOrder == null && retryCount < 3) {
            retryCount++;
            Position position = positions.get((r.nextInt(0, positions.size())));
            String symbol = position.getSymbol();
            Long availableVolume = account.getExistingPositivePositionVolume(symbol) - account.getExistingSellOrderVolume(symbol);
            if(availableVolume <= 0) {
                continue;
            }
            Long volume = Math.min(availableVolume, r.nextInt(1, 100) * 10);
            Integer val = getDynamicPrice(symbol);
            newOrder = toNewOrder(symbol, SELL.name(), val, volume.intValue());
        }
        return newOrder;
    }

    private NewOrder randomShortOrder(Account account) {
        String symbol = symbolContainer.get(r.nextInt(0, symbolContainer.getSymbols().size()));
        Integer val = getDynamicPrice(symbol);
        Integer volume = r.nextInt(1, 10) * 10;
        return toNewOrder(symbol, SHORT.name(), 100, volume);
    }

    private void addToCancel(Order order) {
        if(r.nextInt(0, 9) == 8 && order != null){ //every 10th order
            orderToCancel.add(order.getUuid());
        }
    }

    private void cancelRandom() {
        if (orderToCancel.size() > 10) {
            UUID orderUuid = orderToCancel.remove(r.nextInt(0, orderToCancel.size()));
            Order order = orderManager.getOrder(orderUuid);
            orderManager.cancelOrder(orderUuid, order.getAccount());
        }
    }


    private Route getRoute() {
        int chance = r.nextInt(0, 100);
        if(chance < 48){
            return BUY;
        } else if (chance < 96) {
            return SELL;
        } else {
            return SHORT;
        }
    }

    private Integer getDynamicPrice (String symbol) {
        Integer val = symbolContainer.getQuote(symbol);
        int coefficient = Math.toIntExact(Math.round(val * 0.1d + 0.5 + r.nextGaussian()));
        if(r.nextBoolean()){
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
