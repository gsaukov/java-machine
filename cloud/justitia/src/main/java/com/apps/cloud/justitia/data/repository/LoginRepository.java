package com.apps.cloud.justitia.data.repository;

import com.apps.cloud.justitia.data.entity.Login;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
public interface LoginRepository extends JpaRepository<Login, String> {

    Optional<Login> findByReference(String reference);

    long removeByUsernameAndClientId(String username, String clientId);

}
