package com.apps.searchandpagination.persistance.repository;

import com.apps.searchandpagination.persistance.entity.TradeData;
import com.apps.searchandpagination.persistance.entity.TradeDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TradeDetailsRepository extends JpaRepository<TradeDetails, String>, JpaSpecificationExecutor<TradeDetails> {
    public TradeDetails findByTradeData(TradeData tradeData);
}
