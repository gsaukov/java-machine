package com.apps.searchandpagination.service;

import com.apps.searchandpagination.persistance.query.trade.TradeDetailsCriteria;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class SearchKeeper {
    private final Cache<String, TradeDetailsCriteria> cache;

    public SearchKeeper() {
        this.cache =  CacheBuilder.newBuilder()
                .expireAfterAccess(180, TimeUnit.MINUTES)
                .maximumSize(4096)
                .concurrencyLevel(3)
                .build();
    }


    public String addSearchCriteria(TradeDetailsCriteria criteria){
        String id = UUID.randomUUID().toString();
        cache.put(id, criteria);
        return id;
    }

    public Optional<TradeDetailsCriteria> getSearchCriteria(String searchId){
        return Optional.ofNullable(cache.getIfPresent(searchId));
    }
}
