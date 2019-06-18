package com.apps.searchandpagination.data.creators;

import com.apps.reflection.RandomObjectFiller;
import com.apps.reflection.RandomPattern;
import com.apps.reflection.RandomPatterns;
import com.apps.searchandpagination.persistance.entity.AccountAddress;
import com.apps.searchandpagination.persistance.entity.AccountData;
import com.apps.searchandpagination.persistance.repository.AccountDataRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
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

    @Autowired
    private DataCreationAddress dataCreationAddress;

    private RandomObjectFiller filler = new RandomObjectFiller();

    public void createData() throws IllegalAccessException, InstantiationException {
        List<Object> accountIds = new ArrayList<>();
        List<AccountAddress> addresses = dataCreationAddress.resolveAddressFromFile();
        for(int i = 0; i < 1000; i++){
            AccountData accountData = filler.createAndFill(AccountData.class);
            String accountId = accountData.getAccountId();
            fillAddress(accountData, addresses);
            accountDataRepository.save(accountData);
            accountIds.add(accountId);
        }
        RandomPatterns.addRandomPattern(new RandomPattern("accountId", accountIds));
    }

    private void fillAddress(AccountData accountData, List<AccountAddress> addresses){
        List<AccountAddress> res = new ArrayList<>();
        int size = RandomUtils.nextInt(1, 4);
        for(int i = 0; i < size; i++){
            AccountAddress accountAddress = addresses.get(RandomUtils.nextInt(0, addresses.size()));
            accountAddress.setAccountId(accountData.getAccountId());
            accountAddress.setAddressId(RandomStringUtils.randomAlphanumeric(15).toUpperCase());
            res.add(accountAddress);
        }
        accountData.setAddresses(res);
    }

}
