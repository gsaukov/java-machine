package com.apps.searchandpagination.service;

import com.apps.searchandpagination.persistance.entity.TradeData;
import com.apps.searchandpagination.persistance.repository.TradeDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class TradeDataService {

    @Autowired
    private TradeDataRepository tradeDataRepository;

    public TradeDataService(TradeDataRepository tradeDataRepository) {
        this.tradeDataRepository = tradeDataRepository;
    }

    public Page<TradeData> findPaginated(Pageable pageable) {
        return tradeDataRepository.findAll(pageable);
    }
}