package com.apps.potok.exchange.mkdata;

import com.apps.potok.exchange.core.AbstractExchangeServer;
import com.apps.potok.exchange.core.Exchange;
import com.apps.potok.exchange.core.Order;
import com.apps.potok.exchange.core.SymbolContainer;
import com.apps.potok.soketio.server.AccountManager;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static com.apps.potok.exchange.mkdata.Route.BUY;
import static com.apps.potok.exchange.mkdata.Route.SELL;

@Service
public class MkDataServer extends AbstractExchangeServer {

    @Value("${exchange.order-size}")
    private Integer orderSize;

    @Autowired
    private Exchange exchange;

    private final SymbolContainer symbolContainer;
    private final List<String> symbols;

    public MkDataServer(SymbolContainer symbolContainer) {
        super.setName("MkDataServer");
        this.symbolContainer = symbolContainer;
        this.symbols = symbolContainer.getSymbols();
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
        List<MkData> events = getMkData(size);
        for(MkData event : events) {
            fireEvent(event);
        }
    }

    private void fireEvent(MkData order) {
        exchange.fireOrder(toOrder(order));
    }

    public List<MkData> getMkData(int size){
        List<MkData> mkDatas = new ArrayList<>();

        for(int i = 0 ; i < size; i++){
            mkDatas.add(randomMkData());
        }
        return mkDatas;
    }

    private MkData randomMkData(){
        String symbol = symbols.get(RandomUtils.nextInt(0, symbols.size()));
        Route route = getRoute();
        Integer val = getVal(symbol);
        Integer volume = RandomUtils.nextInt(0, 100) * 10;
        return new MkData(symbol, AccountManager.MK_MAKER, route, val, volume);
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

    private Order toOrder(MkData event){
        return new Order(event.getSymbol(), event.getAccount(), event.getRoute(), event.getVal(), event.getVolume());
    }
}
