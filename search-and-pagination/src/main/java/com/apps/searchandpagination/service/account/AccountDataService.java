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
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < RandomUtils.nextInt(1, 7); i++){
            getRandomHtml(sb, RandomUtils.nextInt(3, 15));
        }
        return sb.toString();
    }

    private void getRandomHtml(StringBuilder sb, int words) {
        if(RandomUtils.nextInt(1, 3)%2 == 0){
            int h = RandomUtils.nextInt(1, 3);
            sb.append("<h"+h+">" + RandomStringUtils.randomAlphabetic(1, 10).toUpperCase() + "</h"+h+">");
        }
        sb.append("<p>");
        for(int i = 0; i < words; i++){
            sb.append(RandomStringUtils.randomAlphabetic(1, 30));
            if(RandomUtils.nextInt(1, 15)%15 == 0){
                sb.append("<br>");
            }else{
                sb.append(" ");
            }
        }
        sb.append("</p>");
    }

    public void setAccountDataRepository(AccountDataRepository accountDataRepository) {
        this.accountDataRepository = accountDataRepository;
    }


}