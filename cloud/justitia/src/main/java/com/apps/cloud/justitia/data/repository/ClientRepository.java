package com.apps.cloud.justitia.data.repository;

import com.apps.cloud.justitia.data.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
public interface ClientRepository extends JpaRepository<Client, String> {

    Optional<Client> findByClientId(String clientId);

    boolean existsByClientId(String clientId);

    long removeByClientId(String clientId);

}
