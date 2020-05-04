package com.apps.depositary.persistance.repository;


import com.apps.depositary.persistance.entity.Deposit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DepositRepository extends JpaRepository<Deposit, UUID> {

}
