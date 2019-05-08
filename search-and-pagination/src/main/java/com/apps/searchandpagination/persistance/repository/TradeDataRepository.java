package com.apps.searchandpagination.persistance.repository;

import com.apps.searchandpagination.persistance.entity.TradeData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface TradeDataRepository extends JpaRepository<TradeData, String>, JpaSpecificationExecutor<TradeData> {

}
