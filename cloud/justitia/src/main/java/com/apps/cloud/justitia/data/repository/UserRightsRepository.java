package com.apps.cloud.justitia.data.repository;

import com.apps.cloud.justitia.data.entity.User;
import com.apps.cloud.justitia.data.entity.UserRights;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface UserRightsRepository extends JpaRepository<UserRights, String> {

    List<UserRights> findByUser(User user);

}
