package com.apps.searchandpagination.service;

import com.apps.reflection.RandomObjectFiller;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {

    private RandomObjectFiller filler = new RandomObjectFiller();

    public Transaction getTransaction(String detailId) {
        Transaction transaction = null;
        try {
            transaction = filler.createAndFill(Transaction.class);
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
