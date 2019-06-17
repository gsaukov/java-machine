package com.apps.searchandpagination.data.creators;

import com.apps.reflection.RandomObjectFiller;
import com.apps.reflection.RandomPattern;
import com.apps.reflection.RandomPatterns;
import com.apps.searchandpagination.persistance.entity.AccountData;
import com.apps.searchandpagination.persistance.repository.AccountDataRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DataCreationAccount {

    @Autowired
    private AccountDataRepository accountDataRepository;

    private RandomObjectFiller filler = new RandomObjectFiller();

    public void createData() throws IllegalAccessException, InstantiationException {
        List<Object> accountIds = new ArrayList<>();
        for(int i = 0; i < 1000; i++){
            String accountId = RandomStringUtils.randomAlphabetic(10, 30);
            accountIds.add(accountId);
        }
        RandomPatterns.addRandomPattern(new RandomPattern("accountId", accountIds));
        for(int i = 0; i < 1000; i++){
            AccountData accountData = filler.createAndFill(AccountData.class);
            accountDataRepository.save(accountData);
        }
    }

}
