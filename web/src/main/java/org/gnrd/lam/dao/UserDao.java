package org.gnrd.lam.dao;

import org.gnrd.lam.entity.UserPO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserDao extends JpaRepository<UserPO, Long> {

    Optional<UserPO> findByName(String name);
}
