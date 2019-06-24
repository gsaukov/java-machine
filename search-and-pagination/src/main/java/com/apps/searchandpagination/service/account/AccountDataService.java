package com.apps.searchandpagination.service.account;

import com.apps.searchandpagination.persistance.entity.AccountData;
import com.apps.searchandpagination.persistance.query.account.AccountDataCriteria;
import com.apps.searchandpagination.persistance.query.account.AccountDataDynamicQuery;
import com.apps.searchandpagination.persistance.repository.AccountDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountDataService {

    @Autowired
    private AccountDataRepository accountDataRepository;

    @Autowired
    private AccountDataDynamicQuery accountDetailsDynamicQuery;

    public Page<AccountData> findAccounts(Pageable page, Optional<AccountDataCriteria> criteria) {
        return accountDetailsDynamicQuery.queryAccounts(page, criteria.orElse(AccountDataCriteria.EMPTY_CRITERIA));
    }

    public AccountData getAccountData(String accountId) {
        return accountDataRepository.findById(accountId).get();
    }

    public void setAccountDataRepository(AccountDataRepository accountDataRepository) {
        this.accountDataRepository = accountDataRepository;
    }


}
