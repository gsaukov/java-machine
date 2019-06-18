package com.apps.searchandpagination.service.account;

import com.apps.searchandpagination.persistance.entity.AccountData;
import com.apps.searchandpagination.persistance.repository.AccountDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountDataService {

    @Autowired
    private AccountDataRepository accountDataRepository;

    public AccountData getAccountData(String accountId) {
        return accountDataRepository.findById(accountId).get();
    }

    public void setAccountDataRepository(AccountDataRepository accountDataRepository) {
        this.accountDataRepository = accountDataRepository;
    }


}
