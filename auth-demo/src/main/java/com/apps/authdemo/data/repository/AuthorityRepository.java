package com.apps.authdemo.data.repository;

import com.apps.authdemo.data.entity.Authority;
import com.apps.authdemo.data.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface AuthorityRepository extends JpaRepository<Authority, String> {

    List<Authority> findByUser(User user);

}
