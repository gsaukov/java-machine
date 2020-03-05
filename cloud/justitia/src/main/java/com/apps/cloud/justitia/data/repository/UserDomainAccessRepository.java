package com.apps.cloud.justitia.data.repository;

import com.apps.cloud.justitia.data.entity.User;
import com.apps.cloud.justitia.data.entity.UserDomainAccess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface UserDomainAccessRepository extends JpaRepository<UserDomainAccess, String> {

    List<UserDomainAccess> findByUser(User user);

}
