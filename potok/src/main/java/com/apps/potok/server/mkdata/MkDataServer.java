package com.apps.potok.server.mkdata;

import com.apps.potok.server.exchange.BidContainer;
import com.apps.potok.server.exchange.SymbolContainer;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MkDataServer {

    private final SymbolContainer symbolContainer;
    private final BidContainer bidContainer;

    public MkDataServer(BidContainer bidContainer, SymbolContainer symbolContainer) {
        this.symbolContainer = symbolContainer;
        this.bidContainer = bidContainer;
    }

    public List<MkData> getMkData(int size){
        List<MkData> mkDatas = new ArrayList<>();
        List<String> symbols = symbolContainer.getSymbols();

        for(int i = 0 ; i < size; i++){

            String symbol = symbols.get(RandomUtils.nextInt(0, symbols.size()));

            MkData mkData = new MkData (symbol, RandomUtils.nextInt(30, 90), Route.BUY);

            mkDatas.add(mkData);
        }
        return mkDatas;
    }

}
