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

package org.gnrd.lam.service.admin.impl;

import org.gnrd.lam.common.constants.AdminStatusEnum;
import org.gnrd.lam.common.exception.BaseException;
import org.gnrd.lam.common.exception.ECode;
import org.gnrd.lam.common.result.ParamPager;
import org.gnrd.lam.common.result.ResultPager;
import org.gnrd.lam.common.tools.Convert;
import org.gnrd.lam.dao.PermissionDao;
import org.gnrd.lam.dao.RoleDao;
import org.gnrd.lam.dao.RolePermissionDao;
import org.gnrd.lam.entity.PermissionPO;
import org.gnrd.lam.entity.RolePO;
import org.gnrd.lam.entity.RolePermissionPO;
import org.gnrd.lam.ro.admin.role.AddRoleRO;
import org.gnrd.lam.ro.admin.role.MapPermissionRO;
import org.gnrd.lam.ro.admin.role.ModifyRoleRO;
import org.gnrd.lam.service.admin.RoleService;
import org.gnrd.lam.vo.admin.PermissionIdVO;
import org.gnrd.lam.vo.admin.RoleItemVO;
import org.gnrd.lam.vo.admin.RolePermissionVO;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Component("roleService")
public class RoleServiceImpl implements RoleService {

    @Resource
    private RoleDao roleDao;
    @Resource
    private RolePermissionDao rolePermissionDao;
    @Resource
    private PermissionDao permissionDao;
    @Resource
    private ModelMapper modelMapper;

    @Override
    public ResultPager<RoleItemVO> list(String nameQuery, Integer state, ParamPager pager) {
        Specification<RolePO> specification = ((root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();
            if (StringUtils.hasText(nameQuery)) {
                Predicate pre1 = criteriaBuilder.like(root.get("name"),
                        Convert.getFuzzyQueryParam(nameQuery));
                Predicate pre2 = criteriaBuilder.like(root.get("code"),
                        Convert.getFuzzyQueryParam(nameQuery));
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.or(pre1, pre2));
            }
            if (state != null) {
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.equal(root.get("state"), state));
            }
            return predicate;
        });
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(pager.getNumber(), pager.getSize(), sort);
        Page<RolePO> rolePOS = roleDao.findAll(specification, pageable);
        Page<RoleItemVO> map =
                rolePOS.map(permissionPO -> modelMapper.map(permissionPO, RoleItemVO.class));
        return ResultPager.of(map);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(AddRoleRO ro) {
        if (roleDao.countByName(ro.getName()) > 0) {
            throw new BaseException(ECode.E_100070);
        }
        if (roleDao.countByCode(ro.getCode()) > 0) {
            throw new BaseException(ECode.E_100071);
        }
        RolePO rolePO = new RolePO();
        rolePO.setName(ro.getName());
        rolePO.setCode(ro.getCode());
        rolePO.setState(AdminStatusEnum.Constants.ENABLED);
        rolePO.setCreatedAt(new Date());
        roleDao.save(rolePO);
        // 添加关联的request_mapping
        if (ro.getPermissionIds() != null) {
            ro.getPermissionIds().forEach(id -> {
                RolePermissionPO rolePermissionPO = new RolePermissionPO();
                rolePermissionPO.setPermissionId(id);
                rolePermissionPO.setRoleId(rolePO.getId());
                rolePermissionPO.setCreatedAt(new Date());
                rolePermissionDao.save(rolePermissionPO);
            });
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void modify(Long id, ModifyRoleRO ro) {
        if (roleDao.countByIdNotAndName(id, ro.getName()) > 0) {
            throw new BaseException(ECode.E_100070);
        }
        if (roleDao.countByIdNotAndCode(id, ro.getCode()) > 0) {
            throw new BaseException(ECode.E_100071);
        }
        RolePO po = roleDao.findById(id).orElseThrow(() -> new BaseException(ECode.E_100072));
        po.setName(ro.getName());
        po.setCode(ro.getCode());
        roleDao.save(po);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        RolePO po = roleDao.findById(id).orElseThrow(() -> new BaseException(ECode.E_100072));
        roleDao.delete(po);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void changeStatus(Long id, Integer status) {
        RolePO po = roleDao.findById(id).orElseThrow(() -> new BaseException(ECode.E_100072));
        if (status.equals(po.getState())) {
            throw new BaseException(ECode.E_100073);
        }
        po.setState(status);
        roleDao.save(po);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void configPermissions(Long id, MapPermissionRO ro) {
        if (CollectionUtils.isEmpty(ro.getPermissionIds())) {
            // 如果是空，则直接删除关联的role_permission记录即可
            rolePermissionDao.deleteByRoleId(id);
        } else {
            final List<RolePermissionPO> needDelete = rolePermissionDao.findByRoleId(id);
            final List<RolePermissionPO> needInsert = new ArrayList<>(ro.getPermissionIds().size());
            for (Long permissionId : ro.getPermissionIds()) {
                if (!containPermissionId(needDelete, permissionId)) {
                    RolePermissionPO requestPO = new RolePermissionPO();
                    requestPO.setRoleId(id);
                    requestPO.setPermissionId(permissionId);
                    requestPO.setCreatedAt(new Date());
                    needInsert.add(requestPO);
                }
            }
            // 删除role_permission信息
            rolePermissionDao.deleteAll(needDelete);
            // 插入role_permission信息
            rolePermissionDao.saveAll(needInsert);
        }
    }

    @Override
    public List<RolePermissionVO> getPermissions() {
        List<PermissionPO> permissionPOS = permissionDao.findAll();
        return permissionPOS.stream().sorted(Comparator.comparing(PermissionPO::getCreatedAt))
                .map(requestMappingPO -> modelMapper.map(requestMappingPO, RolePermissionVO.class))
                .collect(Collectors.toList());
    }

    @Override
    public PermissionIdVO getPermissionIds(Long roleId) {
        List<RolePermissionPO> rolePermissionPOS = rolePermissionDao.findByRoleId(roleId);
        List<String> permissionIds = rolePermissionPOS.stream()
                .map(rolePermissionPO -> rolePermissionPO.getPermissionId().toString())
                .collect(Collectors.toList());
        PermissionIdVO permissionIdVO = new PermissionIdVO();
        permissionIdVO.setPermissionIds(permissionIds);
        return permissionIdVO;
    }

    private boolean containPermissionId(final List<RolePermissionPO> list, Long permissionId) {
        Iterator<RolePermissionPO> iterator = list.iterator();
        while (iterator.hasNext()) {
            RolePermissionPO next = iterator.next();
            if (next.getPermissionId().equals(permissionId)) {
                iterator.remove();
                return true;
            }
        }
        return false;
    }
}
