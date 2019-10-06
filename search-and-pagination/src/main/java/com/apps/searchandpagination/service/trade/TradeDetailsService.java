package com.apps.searchandpagination.service.trade;

import com.apps.searchandpagination.persistance.entity.TradeDetails;
import com.apps.searchandpagination.persistance.repository.TradeDataRepository;
import com.apps.searchandpagination.persistance.repository.TradeDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.stereotype.Service;

@Service
public class TradeDetailsService {

    private TradeDetailsRepository tradeDetailsRepository;

    private TradeDataRepository tradeDataRepository;

    @PostAuthorize("hasDomain(returnObject.domain)")
    public TradeDetails getTrade(String detailId) {
        return tradeDetailsRepository.findByTradeData(tradeDataRepository.findById(detailId).get());
    }

    @Autowired
    public void setTradeDataRepository(TradeDataRepository tradeDataRepository) {
        this.tradeDataRepository = tradeDataRepository;
    }

    @Autowired
    public void setTradeDetailsService(TradeDetailsRepository tradeDetailsRepository) {
        this.tradeDetailsRepository = tradeDetailsRepository;
    }
}
