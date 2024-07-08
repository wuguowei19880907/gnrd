package org.gnrd.lam.dao;

import org.gnrd.lam.entity.PermissionPO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionDao extends JpaRepository<PermissionPO, Long> {
}
