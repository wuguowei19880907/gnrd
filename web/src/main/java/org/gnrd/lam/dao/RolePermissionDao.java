package org.gnrd.lam.dao;

import org.gnrd.lam.entity.RolePermissionPO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolePermissionDao extends JpaRepository<RolePermissionPO, Long> {
}
