package com.apps.searchandpagination.persistance.repository;

import com.apps.searchandpagination.persistance.entity.TradeData;
import com.apps.searchandpagination.persistance.entity.TradeDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TradeDetailsRepository extends JpaRepository<TradeDetails, String>, JpaSpecificationExecutor<TradeDetails> {
    public TradeDetails findByTradeData(TradeData tradeData);

    @Query("select distinct td.domain from TradeDetails td")
    List<String> findAllDomains();

    @Query(value = "SELECT label, value, volume FROM SD_User WHERE id = ?1", nativeQuery = true)
    AnalysedData findByNativeQuery(Integer id);

    public static interface AnalysedData {

        String getLabel();

        String getValue();

        String getVolume();

    }

}
