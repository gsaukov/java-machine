package com.apps.searchandpagination.service;

import com.apps.searchandpagination.persistance.entity.TradeDetails;
import com.apps.searchandpagination.persistance.query.TradeDetailsQuery;
import com.apps.searchandpagination.persistance.repository.TradeDataRepository;
import com.apps.searchandpagination.persistance.repository.TradeDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TradeDetailsService {

    @Autowired
    private TradeDetailsRepository tradeDetailsRepository;

    @Autowired
    private TradeDataRepository tradeDataRepository;

    @Autowired
    private TradeDetailsQuery tradeDetailsQuery;

    public TradeDetailsService(TradeDetailsRepository tradeDetailsRepository) {
        this.tradeDetailsRepository = tradeDetailsRepository;
    }

    public TradeDetails getTrade(String detailId) {

        tradeDetailsQuery.queryTradeDetails();
        return tradeDetailsRepository.findByTradeData(tradeDataRepository.findById(detailId).get());
    }

    public void setTradeDataRepository(TradeDataRepository tradeDataRepository) {
        this.tradeDataRepository = tradeDataRepository;
    }
}
