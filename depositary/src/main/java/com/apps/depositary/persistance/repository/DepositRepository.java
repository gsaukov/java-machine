package com.apps.depositary.persistance.repository;


import com.apps.depositary.persistance.entity.Deposit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Repository
public interface DepositRepository extends JpaRepository<Deposit, UUID> {

    List<Deposit> findByAccountId(String accountId);

    @Modifying
    @Query("update Deposit d set d.fillPrice = ?1, d.quantity = ?2 where d.uuid = ?3")
    void updateDeposit(BigDecimal fillPrice, Integer quantity, UUID id);

}
