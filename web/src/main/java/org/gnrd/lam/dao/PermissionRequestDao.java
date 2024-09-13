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

import org.gnrd.lam.dto.RequestMappingDTO;
import org.gnrd.lam.entity.PermissionRequestPO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PermissionRequestDao extends JpaRepository<PermissionRequestPO, Long> {

    List<PermissionRequestPO> findByPermissionId(Long permissionId);

    @Transactional
    @Query(value = "delete PermissionRequestPO p where p.permissionId = :permissionId")
    @Modifying
    void deleteByPermissionId(@Param("permissionId") Long permissionId);

    @Query(value = "select new org.gnrd.lam.dto.RequestMappingDTO(p.name, p.path, p.method, p.params, p.headers, p.consumes, p.produces) from PermissionRequestPO pr left join pr.requestMappingPO p where pr.permissionId in (:permissionIds) and p.isLost = 0")
    List<RequestMappingDTO> getRequestMappingDTO(@Param("permissionIds") List<Long> permissionIds);
}
