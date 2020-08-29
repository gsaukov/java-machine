package com.apps.potok.exchange.randombehavior;

import com.apps.potok.exchange.account.Account;
import com.apps.potok.exchange.account.AccountManager;
import com.apps.potok.exchange.core.*;
import com.apps.potok.soketio.model.execution.CloseShortPositionRequest;
import com.apps.potok.soketio.model.order.NewOrder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import static com.apps.potok.exchange.core.Route.*;
import static com.apps.potok.exchange.core.Route.SHORT;

public class AccountServer extends AbstractExchangeServer {

    private final SymbolContainer symbolContainer;
    private final AccountManager accountManager;
    private final OrderManager orderManager;
    private final CancelOrderManager cancelOrderManager;
    private final CloseShortManager closeShortManager;
    private final List<UUID> orderToCancel = new ArrayList<>();
    private final List<String> accountIds;
    private ThreadLocalRandom r;

    public AccountServer(ExchangeSpeed exchangeSpeed, SymbolContainer symbolContainer, AccountManager accountManager,
                         OrderManager orderManager, CancelOrderManager cancelOrderManager,
                         CloseShortManager closeShortManager, List<String> accountIds) {
        super.setName("AccountThread");
        super.exchangeSpeed = exchangeSpeed;
        this.cancelOrderManager = cancelOrderManager;
        this.symbolContainer = symbolContainer;
        this.accountManager = accountManager;
        this.orderManager = orderManager;
        this.closeShortManager = closeShortManager;
        this.accountIds = accountIds;
        this.r = ThreadLocalRandom.current();
    }

    @Override
    public void runExchangeServer() {
        Account account = randomAccount();
        NewOrder newOrder = randomOrder(account);
        if(newOrder!=null){
            Order order = orderManager.manageNew(newOrder, account);
            addToCancel(order);
        }
        cancelRandom();
        closeRandomShort(account);
    }

    private void closeRandomShort(Account account) {
        for(Position shortPosition : account.getShortPositions()) {
            Position position = account.getPosition(shortPosition.getSymbol());
            if(position != null) {
                Integer closeAmount = Math.min(Math.abs(shortPosition.getVolume()), position.getVolume());
                closeShortManager.manageCloseShort(toCloseShortPositionRequest(position.getSymbol(), closeAmount), account);
                return;
            }
        }
    }

    @Override
    public void speedControl() {
        exchangeSpeed.orderServerSpeedControl();
    }

    private Account randomAccount() {
        String accountId = accountIds.get(r.nextInt(0, accountIds.size()));
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
        return toNewOrder(symbol, SHORT.name(), val, volume);
    }

    private void addToCancel(Order order) {
        if(r.nextInt(0, 9) > 6 && order != null){ //every 5th order
            orderToCancel.add(order.getUuid());
        }
    }

    private void cancelRandom() {
        if (orderToCancel.size() > 10) {
            UUID orderUuid = orderToCancel.remove(r.nextInt(0, orderToCancel.size()));
            Order order = orderManager.getOrder(orderUuid);
            cancelOrderManager.cancelOrder(orderUuid, order.getAccount());
        }
    }


    private Route getRoute() {
        int chance = r.nextInt(1, 101);
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

    private CloseShortPositionRequest toCloseShortPositionRequest(String symbol, Integer amount){
        CloseShortPositionRequest closeShortPositionRequest = new CloseShortPositionRequest();
        closeShortPositionRequest.setSymbol(symbol);
        closeShortPositionRequest.setAmount(amount);
        return closeShortPositionRequest;
    }
}
