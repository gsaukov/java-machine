package com.apps.searchandpagination.service.trade;

import com.apps.searchandpagination.persistance.entity.TradeData;
import com.apps.searchandpagination.persistance.entity.TradeDetails;
import com.apps.searchandpagination.persistance.query.trade.TradeDetailsCriteria;
import com.apps.searchandpagination.persistance.query.trade.TradeDetailsDynamicQuery;
import com.apps.searchandpagination.persistance.repository.TradeDataRepository;
import com.apps.searchandpagination.security.method.AppAuthorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @Autowired
    private AppAuthorityService appAuthorityService;

    public Page<TradeData> findTrades(Pageable page, Optional<TradeDetailsCriteria> criteria) {
        TradeDetailsCriteria tradeDetailsCriteria = addDomainRestrictions(criteria.orElse(TradeDetailsCriteria.EMPTY_CRITERIA));
        Page<TradeDetails> tradeDetails = tradeDetailsDynamicQuery.queryTrades(page, tradeDetailsCriteria);
        List<TradeData> tradeData = new ArrayList<>();
        tradeDetails.getContent().stream().forEach(t -> tradeData.add(t.getTradeData()));
        return new PageImpl<>(tradeData, page, tradeDetails.getTotalElements());
    }

    public List<String> findAllSymbols(){
        return tradeDataRepository.findAllSymbols();
    }

    private TradeDetailsCriteria addDomainRestrictions(TradeDetailsCriteria tradeDetailsCriteria){
        ArrayList<String> allowedDomains = appAuthorityService.getAvailableDomains(SecurityContextHolder.getContext().getAuthentication());
        tradeDetailsCriteria.setDomains(allowedDomains);
        return tradeDetailsCriteria;
    }

}