package com.apps.depositary.persistance.repository;


import com.apps.depositary.persistance.entity.Deposit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DepositRepository extends JpaRepository<Deposit, UUID> {

    @Modifying
    @Query("update deposit d set d.fillPrice = ?1, d.quantity = ?2 where u.uuid = ?3")
    void updateDeposit(Double fillPrice, Integer quantity, UUID id);

}
