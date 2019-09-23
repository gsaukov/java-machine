package com.apps.searchandpagination.data.creators;

import com.apps.reflection.RandomObjectFiller;
import com.apps.reflection.RandomPattern;
import com.apps.reflection.RandomPatterns;
import com.apps.searchandpagination.persistance.entity.TradeDetails;
import com.apps.searchandpagination.persistance.repository.TradeDetailsRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static javax.transaction.Transactional.TxType.REQUIRES_NEW;

@Component
@Transactional(REQUIRES_NEW)
public class DataCreationTrade {

    private RandomObjectFiller filler = new RandomObjectFiller();

    @Autowired
    private TradeDetailsRepository tradeDetailsRepository;



    public void createData() throws IllegalAccessException, InstantiationException {
        createSymbols();
        createDomains();
        createTrades();
    }

    private void createTrades() throws IllegalAccessException, InstantiationException{
        for(int i = 0; i < 10000; i++) {
            TradeDetails tradeDetails = filler.createAndFill(TradeDetails.class);
            tradeDetails.setMix(getRandomWords(5));
            tradeDetails.setDetails(getRandomWords(10));
            tradeDetailsRepository.save(tradeDetails);
        }
    }

    private void createDomains() {
        List<Object> domains = Arrays.asList("Mercury","Venus","Earth","Mars","Jupiter","Saturn","Uranus","Neptune","Pluto");
        RandomPatterns.addRandomPattern(new RandomPattern("domain", domains));
    }

    private void createSymbols(){
        List<Object> symbols = new ArrayList<>();
        for(int i = 0; i < 1000; i++){
            symbols.add(RandomStringUtils.randomAlphabetic(3, 5).toUpperCase());
        }
        RandomPatterns.addRandomPattern(new RandomPattern("symbol", symbols));
    }

    private String getRandomWords(int words){
        String res = "";
        for(int i = 0; i < words; i++){
            res += RandomStringUtils.randomAlphabetic(1, 30);
            res += " ";
        }
        return res;
    }
}
