/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * Copyright 2023 gnrd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.gnrd.lam.dao;

import org.gnrd.lam.entity.PermissionPO;
import org.gnrd.lam.entity.UserRolePO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface UserRoleDao extends JpaRepository<UserRolePO, Long> {

	@Query(value = "select p from UserRolePO ur left join ur.role r left join r.rolePermissionPOSet rp left join rp.permissionPO p where ur.userId = :userId")
	Set<PermissionPO> findByUserId(@Param("userId") Long userId);
}
