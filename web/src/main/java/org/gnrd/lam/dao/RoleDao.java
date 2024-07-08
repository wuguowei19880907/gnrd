package org.gnrd.lam.dao;

import org.gnrd.lam.entity.RolePO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleDao extends JpaRepository<RolePO, Long> {
}
