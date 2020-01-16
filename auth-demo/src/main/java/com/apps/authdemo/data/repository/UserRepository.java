package com.apps.authdemo.data.repository;

import com.apps.authdemo.data.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByUsername(String username);

}
