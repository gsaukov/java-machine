package com.apps.searchandpagination.persistance.repository;

import com.apps.searchandpagination.persistance.entity.AccountAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountAddressRepository extends JpaRepository<AccountAddress, String>, JpaSpecificationExecutor<AccountAddress> {
    @Query("select distinct aa.city from AccountAddress aa")
    List<String> findAllCities();

    @Query("select distinct aa.state from AccountAddress aa")
    List<String> findAllStates();
}
