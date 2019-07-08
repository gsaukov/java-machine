package com.apps.searchandpagination.service.account;

import com.apps.searchandpagination.persistance.entity.AccountData;
import com.apps.searchandpagination.persistance.query.account.AccountDataCriteria;
import com.apps.searchandpagination.persistance.query.account.AccountDataDynamicQuery;
import com.apps.searchandpagination.persistance.repository.AccountDataRepository;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.RandomStringUtils;
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

    public String getAccountVatData(String accountId) {
        String res = "";
        for(int i = 0; i < RandomUtils.nextInt(3, 7); i++){
            res += getRandomHtml(RandomUtils.nextInt(3, 15));

        }
        return res ;
    }

    private String getRandomHtml(int words){
        String res = "";
        for(int i = 0; i < words; i++){
            res += RandomStringUtils.randomAlphabetic(1, 30);
            if(RandomUtils.nextInt(1, 15)%15 == 0){
                res += "<br>";
            }else{
                res += " ";
            }
        }
        return wrapRandomly(res);
    }

    private String wrapRandomly(String res) {
        res = "<p>" + res + "</p>";
        if(RandomUtils.nextInt(1, 3)%2 == 0){
            int h = RandomUtils.nextInt(1, 3);
            res = "<h"+h+">" + RandomStringUtils.randomAlphabetic(1, 10).toUpperCase() + "</h"+h+">" + res;
        }
        return res;
    }

    public void setAccountDataRepository(AccountDataRepository accountDataRepository) {
        this.accountDataRepository = accountDataRepository;
    }


}