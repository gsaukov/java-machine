package com.apps.searchandpagination.service;

import com.apps.searchandpagination.persistance.query.trade.TradeDetailsCriteria;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SearchKeeper {
    private final ConcurrentHashMap<String, TradeDetailsCriteria> keeper = new ConcurrentHashMap<>();

    public String addSearchCriteria(TradeDetailsCriteria criteria){
        String id = UUID.randomUUID().toString();
        keeper.put(id, criteria);
        return id;
    }

    public Optional<TradeDetailsCriteria> getSearchCriteria(String searchId){
        return Optional.ofNullable(keeper.get(searchId));
    }
}
