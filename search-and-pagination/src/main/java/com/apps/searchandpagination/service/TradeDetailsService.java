package com.apps.searchandpagination.service;

import com.apps.reflection.RandomObjectFiller;
import com.apps.searchandpagination.persistance.entity.TradeDetails;
import com.apps.searchandpagination.persistance.repository.TradeDetailsRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TradeDetailsService {

    private RandomObjectFiller filler = new RandomObjectFiller();

    @Autowired
    private TradeDetailsRepository tradeDetailsRepository;

    public TradeDetailsService(TradeDetailsRepository tradeDetailsRepository) {
        this.tradeDetailsRepository = tradeDetailsRepository;
    }

    public TradeDetails getTransaction(String detailId) {
        TradeDetails transaction = null;
        try {
            transaction = filler.createAndFill(TradeDetails.class);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        transaction.setId(detailId);
        transaction.setMix(getRandomWords(5));
        transaction.setDetails(getRandomWords(10));
        return transaction;
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
