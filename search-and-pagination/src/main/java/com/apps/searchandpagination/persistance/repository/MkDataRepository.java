package com.apps.searchandpagination.persistance.repository;

import com.apps.searchandpagination.persistance.entity.MkData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MkDataRepository extends JpaRepository<MkData, String> {

}
