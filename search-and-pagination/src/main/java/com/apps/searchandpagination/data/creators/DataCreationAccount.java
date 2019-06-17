package com.apps.searchandpagination.data.creators;

import com.apps.reflection.RandomObjectFiller;
import com.apps.reflection.RandomPattern;
import com.apps.reflection.RandomPatterns;
import com.apps.searchandpagination.persistance.entity.AccountData;
import com.apps.searchandpagination.persistance.repository.AccountDataRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static javax.transaction.Transactional.TxType.REQUIRES_NEW;

@Component
@Transactional(REQUIRES_NEW)
public class DataCreationAccount {

    @Autowired
    private AccountDataRepository accountDataRepository;

    private RandomObjectFiller filler = new RandomObjectFiller();

    public void createData() throws IllegalAccessException, InstantiationException {
        List<Object> accountIds = new ArrayList<>();
        for(int i = 0; i < 1000; i++){
            AccountData accountData = filler.createAndFill(AccountData.class);
            String accountId = accountData.getAccountId();
            accountData.getAddresses().stream().forEach(a -> a.setAccountId(accountId));
            accountDataRepository.save(accountData);
            accountIds.add(accountId);
        }
        RandomPatterns.addRandomPattern(new RandomPattern("accountId", accountIds));
    }

}
