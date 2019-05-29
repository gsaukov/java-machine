package com.apps.searchandpagination.service.trade;

import com.apps.searchandpagination.persistance.entity.TradeData;
import com.apps.searchandpagination.persistance.entity.TradeDetails;
import com.apps.searchandpagination.persistance.query.trade.TradeDetailsCriteria;
import com.apps.searchandpagination.persistance.query.trade.TradeDetailsDynamicQuery;
import com.apps.searchandpagination.persistance.repository.TradeDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TradeDataService {

    @Autowired
    private TradeDataRepository tradeDataRepository;

    @Autowired
    private TradeDetailsDynamicQuery tradeDetailsDynamicQuery;

    public TradeDataService(TradeDataRepository tradeDataRepository) {
        this.tradeDataRepository = tradeDataRepository;
    }

    public Page<TradeData> findTrades(Pageable page, Optional<TradeDetailsCriteria> criteria) {
        Page<TradeDetails> tradeDetails = tradeDetailsDynamicQuery.queryTrades(page, criteria.orElse(TradeDetailsCriteria.EMPTY_CRITERIA));
        List<TradeData> tradeData = new ArrayList<>();
        if(!tradeDetails.getContent().isEmpty() && tradeDetails.getContent().get(0).getClass().isAssignableFrom(TradeData.class)){
//            (TradeDetails)tradeDetails.getContent().get(0)
//            tradeData = tradeDetails.getContent().stream().map(t -> (TradeData) t);
        } else {
            tradeDetails.getContent().stream().forEach(t -> tradeData.add(t.getTradeData()));
        }


        return new PageImpl<>(tradeData, page, tradeDetails.getTotalElements());
    }

}