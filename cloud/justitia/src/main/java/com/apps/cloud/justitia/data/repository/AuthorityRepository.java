package com.apps.cloud.justitia.data.repository;

import com.apps.cloud.justitia.data.entity.Authority;
import com.apps.cloud.justitia.data.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface AuthorityRepository extends JpaRepository<Authority, String> {

    List<Authority> findByUser(User user);

    boolean existsByUserAndAuthority(User user, String authority);

    long removeByUserAndAuthority(User user, String authority);

}
