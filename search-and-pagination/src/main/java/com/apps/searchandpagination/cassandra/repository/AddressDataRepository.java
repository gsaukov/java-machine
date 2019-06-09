package com.apps.searchandpagination.cassandra.repository;

import com.apps.searchandpagination.cassandra.entity.AddressData;
import com.apps.searchandpagination.cassandra.entity.AddressDataKey;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressDataRepository extends CassandraRepository<AddressData, AddressDataKey> {
}
