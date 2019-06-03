package com.apps.searchandpagination.service;

import com.apps.searchandpagination.persistance.query.trade.TradeDetailsCriteria;
import com.google.common.cache.CacheBuilder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SearchKeeper {
    private final ConcurrentHashMap<String, TradeDetailsCriteria> keeper = new ConcurrentHashMap<>();
    private final CacheBuilder cacheBuilder = CacheBuilder.newBuilder();

    public String addSearchCriteria(TradeDetailsCriteria criteria){
//        cacheBuilder.
        String id = UUID.randomUUID().toString();
        keeper.put(id, criteria);
        return id;
    }

    public Optional<TradeDetailsCriteria> getSearchCriteria(String searchId){
        return Optional.ofNullable(keeper.get(searchId));
    }
}
