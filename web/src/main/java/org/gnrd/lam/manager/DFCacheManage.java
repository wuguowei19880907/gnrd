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

package org.gnrd.lam.manager;

import org.gnrd.lam.cache.PermissionCache;
import org.gnrd.lam.cache.PermissionRequestCache;
import org.gnrd.lam.cache.RequestPermissionCache;
import org.gnrd.lam.cache.RoleCache;
import org.gnrd.lam.common.exception.BaseException;
import org.gnrd.lam.common.exception.ECode;
import org.gnrd.lam.dao.PermissionDao;
import org.gnrd.lam.dao.RoleDao;
import org.gnrd.lam.dao.UserDao;
import org.gnrd.lam.dao.UserRoleDao;
import org.gnrd.lam.dto.LoginMenuDTO;
import org.gnrd.lam.dto.LoginPermissionDTO;
import org.gnrd.lam.dto.LoginUserDTO;
import org.gnrd.lam.dto.RequestMappingDTO;
import org.gnrd.lam.dto.RequestPermissionDTO;
import org.gnrd.lam.entity.MenuPO;
import org.gnrd.lam.entity.PermissionPO;
import org.gnrd.lam.entity.RequestMappingPO;
import org.gnrd.lam.entity.UserPO;
import org.gnrd.lam.repo.RequestMappingRepo;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component("dfCacheManage")
public class DFCacheManage {

    @Resource
    private UserDao userDao;
    @Resource
    private UserRoleDao userRoleDao;
    @Resource
    private RoleDao roleDao;
    @Resource
    private PermissionDao permissionDao;
    @Resource
    private RequestMappingRepo requestMappingRepo;
    @Resource
    private ModelMapper modelMapper;

    @CacheEvict(value = "login-user", cacheManager = "cacheManager", allEntries = true)
    public void clearLoginUser() {

    }

    @Cacheable(value = "login-user", key = "'user:'+#userId", cacheManager = "cacheManager")
    public LoginUserDTO getLoginUser(Long userId) {
        UserPO userPO =
                userDao.findById(userId).orElseThrow(() -> new BaseException(ECode.E_000001));
        return modelMapper.map(userPO, LoginUserDTO.class);
    }

    @CachePut(value = "login-user", key = "'user:'+#userId", cacheManager = "cacheManager")
    public LoginUserDTO putLoginUser(Long userId, LoginUserDTO dto) {
        return dto;
    }

    @CachePut(value = "login-user", key = "'menus:'+#userId", cacheManager = "cacheManager")
    public List<LoginMenuDTO> putLoginUserMenus(Long userId, List<LoginMenuDTO> list) {
        return list;
    }

    @Cacheable(value = "login-user", key = "'menus:'+#userId", cacheManager = "cacheManager")
    public List<LoginMenuDTO> getLoginUserMenus(Long userId) {
        Set<MenuPO> menuPOSet = userRoleDao.findMenusByUserId(userId);
        return menuPOSet.stream().sorted(Comparator.comparing(MenuPO::getSort))
                .map(menuPO -> modelMapper.map(menuPO, LoginMenuDTO.class))
                .collect(Collectors.toList());
    }

    @CachePut(value = "login-user", key = "'permissions:'+#userId", cacheManager = "cacheManager")
    public List<LoginPermissionDTO> putLoginUserPermissions(Long userId,
            List<LoginPermissionDTO> list) {
        return list;
    }

    @Cacheable(value = "login-user", key = "'permissions:'+#userId", cacheManager = "cacheManager")
    public List<LoginPermissionDTO> getLoginUserPermissions(Long userId) {
        Set<PermissionPO> permissionPOSet = userRoleDao.findPermissionsByUserId(userId);
        return permissionPOSet.stream().sorted(Comparator.comparing(PermissionPO::getId))
                .map(permissionPO -> modelMapper.map(permissionPO, LoginPermissionDTO.class))
                .collect(Collectors.toList());
    }

    @CachePut(value = "login-user", key = "'requestMappings:'+#userId",
            cacheManager = "cacheManager")
    public List<RequestMappingDTO> putLoginUserRequestMappings(Long userId,
            List<RequestMappingDTO> list) {
        return list;
    }

    @Cacheable(value = "login-user", key = "'requestMappings:'+#userId",
            cacheManager = "cacheManager")
    public List<RequestMappingDTO> getLoginUserRequestMappings(Long userId) {
        Set<RequestMappingPO> requestMappingPOS = userRoleDao.findRequestMappingsByUserId(userId);
        return requestMappingPOS.stream().sorted(Comparator.comparing(RequestMappingPO::getId))
                .map(requestMappingPO -> modelMapper.map(requestMappingPO, RequestMappingDTO.class))
                .collect(Collectors.toList());
    }

    @Cacheable(value = "base", key = "'roles:'+#roleId", cacheManager = "cacheManager")
    public RoleCache getRole(Long roleId) {
        return roleDao.findCache(roleId);
    }

    @CachePut(value = "base", key = "'roles:'+#roleId", cacheManager = "cacheManager")
    public RoleCache putRole(Long roleId, RoleCache cache) {
        return cache;
    }

    @Cacheable(value = "base", key = "'permissions:'+#permissionId", cacheManager = "cacheManager")
    public PermissionCache getPermission(Long permissionId) {
        return permissionDao.findCache(permissionId);
    }

    @CachePut(value = "base", key = "'permissions:'+#permissionId", cacheManager = "cacheManager")
    public PermissionCache putPermission(Long permissionId, PermissionCache cache) {
        return cache;
    }

    @Cacheable(value = "base", key = "'permission-request:'+#permissionId",
            cacheManager = "cacheManager")
    public PermissionRequestCache getPermissionRequest(Long permissionId) {
        Set<String> requests = permissionDao.findRequestCache(permissionId);
        return new PermissionRequestCache(permissionId, requests);
    }

    @CachePut(value = "base", key = "'permission-request:'+#permissionId",
            cacheManager = "cacheManager")
    public PermissionRequestCache putPermissionRequest(Long permissionId,
            PermissionRequestCache cache) {
        return cache;
    }

    @Cacheable(value = "base", key = "'request-permission:'+#requestName",
            cacheManager = "cacheManager")
    public RequestPermissionCache getRequestPermission(String requestName) {
        List<RequestPermissionDTO> requestPermissions =
                requestMappingRepo.getRequestPermissions(requestName);
        if (CollectionUtils.isEmpty(requestPermissions)) {
            throw new BaseException(ECode.E_100064);
        }
        Map<String, Set<Long>> map1 = requestPermissions.stream().collect(Collectors.groupingBy(
                RequestPermissionDTO::getRequestName,
                Collectors.mapping(RequestPermissionDTO::getPermissionId, Collectors.toSet())));
        Set<Long> set = map1.get(requestName);
        return new RequestPermissionCache(set);
    }

    @CachePut(value = "base", key = "'request-permission:'+#requestName",
            cacheManager = "cacheManager")
    public RequestPermissionCache putRequestPermission(String requestName,
            RequestPermissionCache cache) {
        return cache;
    }
}
