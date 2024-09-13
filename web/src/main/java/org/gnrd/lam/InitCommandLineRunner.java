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

package org.gnrd.lam;

import org.gnrd.lam.cache.PermissionCache;
import org.gnrd.lam.cache.PermissionRequestCache;
import org.gnrd.lam.cache.RequestPermissionCache;
import org.gnrd.lam.cache.RoleCache;
import org.gnrd.lam.dao.PermissionDao;
import org.gnrd.lam.dao.RoleDao;
import org.gnrd.lam.dto.PermissionRequestDTO;
import org.gnrd.lam.dto.RequestPermissionDTO;
import org.gnrd.lam.entity.PermissionPO;
import org.gnrd.lam.entity.RolePO;
import org.gnrd.lam.manager.DFCacheManage;
import org.gnrd.lam.repo.RequestMappingRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class InitCommandLineRunner implements CommandLineRunner {

    @Resource
    private DFCacheManage dfCacheManage;
    @Resource
    private RoleDao roleDao;
    @Resource
    private PermissionDao permissionDao;
    @Resource
    private RequestMappingRepo requestMappingRepo;

    @Override
    public void run(String... args) throws Exception {
        // 缓存角色信息
        List<RolePO> roles = roleDao.findAll();
        roles.forEach(p -> dfCacheManage.putRole(p.getId(),
                new RoleCache(p.getId(), p.getName(), p.getCode(), p.getState())));
        // 缓存权限信息
        List<PermissionPO> permissions = permissionDao.findAll();
        permissions.forEach(p -> dfCacheManage.putPermission(p.getId(),
                new PermissionCache(p.getId(), p.getName(), p.getCode(), p.getState())));
        // 缓存权限<-->request mapping信息
        Set<PermissionRequestDTO> requests = permissionDao.findRequests();
        Map<Long, Set<String>> map0 = requests.stream().collect(Collectors.groupingBy(
                PermissionRequestDTO::getPermissionId,
                Collectors.mapping(PermissionRequestDTO::getRequestName, Collectors.toSet())));
        map0.forEach((key, value) -> dfCacheManage.putPermissionRequest(key,
                new PermissionRequestCache(key, value)));
        // 缓存request mapping<-->权限
        List<RequestPermissionDTO> requestPermissions =
                requestMappingRepo.getRequestPermissions(null);
        Map<String, Set<Long>> map1 = requestPermissions.stream().collect(Collectors.groupingBy(
                RequestPermissionDTO::getRequestName,
                Collectors.mapping(RequestPermissionDTO::getPermissionId, Collectors.toSet())));
        map1.forEach((key, value) -> dfCacheManage.putRequestPermission(key,
                new RequestPermissionCache(value)));
    }
}
