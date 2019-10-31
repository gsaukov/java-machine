package com.apps.potok.server.mkdata;

import com.apps.potok.server.exchange.AskContainer;
import com.apps.potok.server.exchange.BidContainer;
import com.apps.potok.server.exchange.SymbolContainer;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.apps.potok.server.mkdata.Route.BUY;
import static com.apps.potok.server.mkdata.Route.SELL;

@Service
public class MkDataServer {

    private final SymbolContainer symbolContainer;
    private final BidContainer bidContainer;
    private final AskContainer askContainer;

    public MkDataServer(BidContainer bidContainer, AskContainer askContainer, SymbolContainer symbolContainer) {
        this.symbolContainer = symbolContainer;
        this.bidContainer = bidContainer;
        this.askContainer = askContainer;
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
        Integer val = getVal(symbol, route);
        return new MkData(symbol, val, route, "mk_maker");
    }

    private Route getRoute() {
        if(RandomUtils.nextBoolean()){
            return BUY;
        } else {
            return SELL;
        }
    }

    private Integer getVal(String symbol, Route route) {
        Integer val = symbolContainer.getQuote(symbol);
        if(!BUY.equals(route)){
            return RandomUtils.nextInt(1, val);
        } else {
            return RandomUtils.nextInt(val - 1, 100);
        }
    }
}
