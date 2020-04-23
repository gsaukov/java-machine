package com.apps.depositary.persistance.repository;

import com.apps.depositary.persistance.entity.Execution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ExecutionRepository extends JpaRepository<Execution, UUID> {

}
