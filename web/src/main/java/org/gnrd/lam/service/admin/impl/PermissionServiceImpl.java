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

import org.gnrd.lam.common.exception.BaseException;
import org.gnrd.lam.common.exception.ECode;
import org.gnrd.lam.common.result.ParamPager;
import org.gnrd.lam.common.result.ResultPager;
import org.gnrd.lam.common.tools.Convert;
import org.gnrd.lam.dao.PermissionDao;
import org.gnrd.lam.entity.PermissionPO;
import org.gnrd.lam.ro.admin.permission.AddPermissionRO;
import org.gnrd.lam.service.admin.PermissionService;
import org.gnrd.lam.vo.admin.PermissionItemVO;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.persistence.criteria.Predicate;

@Component("permissionService")
public class PermissionServiceImpl implements PermissionService {


    @Resource
    private PermissionDao permissionDao;
    @Resource
    private ModelMapper modelMapper;

    @Override
    public ResultPager<PermissionItemVO> list(String queryName, Integer status, ParamPager pager) {
        Specification<PermissionPO> specification = ((root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();
            if (StringUtils.hasText(queryName)) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get("name"),
                        Convert.getFuzzyQueryParam(queryName)));
            }
            if (status != null) {
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.equal(root.get("state"), status));
            }
            return predicate;
        });
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(pager.getNumber(), pager.getSize(), sort);
        Page<PermissionPO> permissionPOS = permissionDao.findAll(specification, pageable);
        Page<PermissionItemVO> map = permissionPOS
                .map(permissionPO -> modelMapper.map(permissionPO, PermissionItemVO.class));
        return ResultPager.of(map);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(AddPermissionRO ro) {
        if (permissionDao.countByName(ro.getName()) > 0) {
            throw new BaseException(ECode.E_100050);
        }
        if (permissionDao.countByCode(ro.getCode()) > 0) {
            throw new BaseException(ECode.E_100051);
        }
        // UserPO userPO = new UserPO();
        // userPO.setName(ro.getName());
        // userPO.setPassword(passwordEncoder.encode(ro.getPassword()));
        // userPO.setState(UserStatusEnum.Constants.ENABLED);
        // userPO.setPhone(ro.getPhone());
        // userPO.setSuperAdmin(0);
        // userPO.setCreatedAt(new Date());
        // userDao.save(userPO);
    }
}
