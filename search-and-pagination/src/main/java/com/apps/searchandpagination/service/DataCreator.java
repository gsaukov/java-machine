package com.apps.searchandpagination.service;

import com.apps.reflection.RandomObjectFiller;
import com.apps.searchandpagination.persistance.entity.TradeData;
import com.apps.searchandpagination.persistance.entity.TradeDetails;
import com.apps.searchandpagination.persistance.repository.TradeDataRepository;
import com.apps.searchandpagination.persistance.repository.TradeDetailsRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class DataCreator {

    private RandomObjectFiller filler = new RandomObjectFiller();

    @Autowired
    private TradeDataRepository mkDataRepository;

    @Autowired
    private TradeDetailsRepository tradeDetailsRepository;

    @PostConstruct
    private void postConstruct(){
        try {
            fillObjects();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void fillObjects() throws IllegalAccessException, InstantiationException {
        for(int i = 0; i < 1000; i++){
            TradeDetails tradeDetails = filler.createAndFill(TradeDetails.class);
            tradeDetails.setMix(getRandomWords(5));
            tradeDetails.setDetails(getRandomWords(10));
            tradeDetailsRepository.save(tradeDetails);
        }
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