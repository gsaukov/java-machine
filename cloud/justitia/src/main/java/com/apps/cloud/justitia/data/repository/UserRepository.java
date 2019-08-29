package com.apps.cloud.justitia.data.repository;

import com.apps.cloud.justitia.data.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    long removeByUsername(String username);

}
