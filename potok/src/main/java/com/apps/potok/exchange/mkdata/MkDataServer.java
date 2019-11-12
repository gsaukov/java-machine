package com.apps.potok.exchange.mkdata;

import com.apps.potok.exchange.core.SymbolContainer;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static com.apps.potok.exchange.mkdata.Route.BUY;
import static com.apps.potok.exchange.mkdata.Route.SELL;

@Service
public class MkDataServer {

    private final SymbolContainer symbolContainer;

    public MkDataServer(SymbolContainer symbolContainer) {
        this.symbolContainer = symbolContainer;
    }

    public List<MkData> getMkData(int size){
        List<MkData> mkDatas = new ArrayList<>();

        for(int i = 0 ; i < size; i++){
            mkDatas.add(randomMkData());
        }
        return mkDatas;
    }

    private MkData randomMkData(){
        List<String> symbols = symbolContainer.getSymbols();
        String symbol = symbols.get(RandomUtils.nextInt(0, symbols.size()));
        Route route = getRoute();
        Integer val = getVal(symbol);
        Integer volume = RandomUtils.nextInt(0, 100) * 10;
        return new MkData(symbol, "mk_maker", route, val, volume);
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
}
