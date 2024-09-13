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

import org.gnrd.lam.cache.PermissionCache;
import org.gnrd.lam.dto.PermissionRequestDTO;
import org.gnrd.lam.entity.PermissionPO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface PermissionDao
        extends JpaRepository<PermissionPO, Long>, JpaSpecificationExecutor<PermissionPO> {

    long countByName(String name);

    long countByCode(String code);

    long countByIdNotAndName(Long id, String name);

    long countByIdNotAndCode(Long id, String phone);

    @Query(value = "select new org.gnrd.lam.cache.PermissionCache(p.id,p.name,p.code,p.state) from PermissionPO p where p.id =:id ")
    PermissionCache findCache(@Param("id") Long id);

    @Query(value = "select p.name from PermissionRequestPO pr left join pr.requestMappingPO p where pr.permissionId =:id ")
    Set<String> findRequestCache(@Param("id") Long id);

    @Query(value = "select new org.gnrd.lam.dto.PermissionRequestDTO(pr.permissionId,p.name) from PermissionRequestPO pr left join pr.requestMappingPO p")
    Set<PermissionRequestDTO> findRequests();
}
