package com.apps.potok.server.mkdata;

import com.apps.potok.server.alert.AlertContainer;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MkDataServer {

    AlertContainer alertContainer;

    public MkDataServer(AlertContainer alertContainer) {
        this.alertContainer = alertContainer;
    }

    public List<MkData> getMkData(int size){
        List<MkData> mkDatas = new ArrayList<>();
        List<String> symbols = alertContainer.getSymbols();

        for(int i = 0 ; i < size; i++){

            String symbol = symbols.get(RandomUtils.nextInt(0, symbols.size()));

            MkData mkData = new MkData (symbol, RandomUtils.nextInt(30, 90));

            mkDatas.add(mkData);
        }
        return mkDatas;
    }

}
