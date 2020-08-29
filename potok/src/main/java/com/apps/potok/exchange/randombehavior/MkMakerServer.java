package com.apps.potok.exchange.randombehavior;

import com.apps.potok.exchange.account.Account;
import com.apps.potok.exchange.core.*;
import com.apps.potok.exchange.account.AccountManager;
import com.apps.potok.soketio.model.order.NewOrder;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static com.apps.potok.exchange.core.Route.BUY;
import static com.apps.potok.exchange.core.Route.SELL;

@Service
public class MkMakerServer extends AbstractExchangeServer {

    @Value("${exchange.order-size}")
    private Integer orderSize;

    private final OrderManager orderManager;

    private final SymbolContainer symbolContainer;

    private final AccountManager accountManager;

    private Account mkMaker;

    public MkMakerServer(SymbolContainer symbolContainer, AccountManager accountManager, OrderManager orderManager) {
        super.setName("MkDataServer");
        this.symbolContainer = symbolContainer;
        this.accountManager = accountManager;
        this.orderManager = orderManager;
    }

    @Override
    public void runExchangeServer() {
        pullMkData(orderSize);
    }

    @Override
    public void speedControl() {
        exchangeSpeed.mkDataServerSpeedControl();
    }

    private void pullMkData(int size) {
        List<NewOrder> events = getMkData(size);
        for(NewOrder event : events) {
            fireEvent(event);
        }
    }

    private void fireEvent(NewOrder newOrder) {
        orderManager.manageNew(newOrder, mkMaker);
    }

    public List<NewOrder> getMkData(int size){
        if(mkMaker == null) {
            this.mkMaker = accountManager.getAccount(AccountManager.MK_MAKER);
        }
        List<NewOrder> mkData = new ArrayList<>();
        for(int i = 0 ; i < size; i++){
            mkData.add(randomMkData());
        }
        return mkData;
    }

    private NewOrder randomMkData(){
        String symbol = symbolContainer.get(RandomUtils.nextInt(0, symbolContainer.getSymbols().size()));
        Route route = getRoute();
        Integer val = getVal(symbol);
        Integer volume = RandomUtils.nextInt(1, 100) * 10;
        if(volume > (mkMaker.getExistingPositivePositionVolume(symbol) + mkMaker.getExistingSellOrderVolume(symbol))) { // if position  is less than order then buy.
            route = BUY;
        }
        return toNewOrder(symbol, route.name(), val, volume);
    }

    private Route getRoute() { //MkData Can be only buy or sell
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

    private NewOrder toNewOrder(String symbol, String route, Integer val, Integer volume){
        NewOrder newOrder = new NewOrder();
        newOrder.setSymbol(symbol);
        newOrder.setRoute(route);
        newOrder.setVal(val);
        newOrder.setVolume(volume);
        return newOrder;
    }
}
