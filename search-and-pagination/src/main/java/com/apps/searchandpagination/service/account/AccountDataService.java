package com.apps.searchandpagination.service.account;

import com.apps.searchandpagination.persistance.entity.AccountData;
import com.apps.searchandpagination.persistance.repository.AccountDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AccountDataService {

    @Autowired
    private AccountDataRepository accountDataRepository;

    public Page<AccountData> findTrades(Pageable page, Optional<TradeDetailsCriteria> criteria) {
        Page<TradeDetails> tradeDetails = tradeDetailsDynamicQuery.queryTrades(page, criteria.orElse(TradeDetailsCriteria.EMPTY_CRITERIA));
        List<TradeData> tradeData = new ArrayList<>();
        tradeDetails.getContent().stream().forEach(t -> tradeData.add(t.getTradeData()));
        return new PageImpl<>(tradeData, page, tradeDetails.getTotalElements());
    }

    public AccountData getAccountData(String accountId) {
        return accountDataRepository.findById(accountId).get();
    }

    public void setAccountDataRepository(AccountDataRepository accountDataRepository) {
        this.accountDataRepository = accountDataRepository;
    }


}
