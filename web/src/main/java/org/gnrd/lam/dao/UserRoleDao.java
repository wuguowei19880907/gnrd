package org.gnrd.lam.dao;

import org.gnrd.lam.entity.UserRolePO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleDao extends JpaRepository<UserRolePO, Long> {
}
