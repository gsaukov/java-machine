package com.apps.searchandpagination.persistance.repository;

import com.apps.searchandpagination.persistance.entity.AccountData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountDataRepository extends JpaRepository<AccountData, String>, JpaSpecificationExecutor<AccountData> {

}
