package com.apps.searchandpagination.service.trade;

import com.apps.searchandpagination.persistance.entity.TradeDetails;
import com.apps.searchandpagination.persistance.repository.TradeDataRepository;
import com.apps.searchandpagination.persistance.repository.TradeDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
public class TradeDetailsService {

    @Autowired
    private TradeDetailsRepository tradeDetailsRepository;

    @Autowired
    private TradeDataRepository tradeDataRepository;

//    @PreAuthorize("hasPermission(#detailId, 'Foo', 'read')")
//    @PostAuthorize("hasPermission(returnObject.domain, 'read')")
    @PostAuthorize("hasDomain(returnObject.domain)")
    public TradeDetails getTrade(String detailId) {
        return tradeDetailsRepository.findByTradeData(tradeDataRepository.findById(detailId).get());
    }

    public void setTradeDataRepository(TradeDataRepository tradeDataRepository) {
        this.tradeDataRepository = tradeDataRepository;
    }

    public void setTradeDetailsService(TradeDetailsRepository tradeDetailsRepository) {
        this.tradeDetailsRepository = tradeDetailsRepository;
    }
}
