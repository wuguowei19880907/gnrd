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
import org.gnrd.lam.dao.PermissionRequestDao;
import org.gnrd.lam.dao.RequestMappingDao;
import org.gnrd.lam.entity.PermissionPO;
import org.gnrd.lam.entity.PermissionRequestPO;
import org.gnrd.lam.entity.RequestMappingPO;
import org.gnrd.lam.ro.admin.permission.AddPermissionRO;
import org.gnrd.lam.ro.admin.permission.MapRequestRO;
import org.gnrd.lam.ro.admin.permission.ModifyPermissionRO;
import org.gnrd.lam.service.admin.PermissionService;
import org.gnrd.lam.vo.admin.permission.PermissionItemVO;
import org.gnrd.lam.vo.admin.permission.RequestIdVO;
import org.gnrd.lam.vo.admin.permission.RequestMappingVO;
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

@Component("permissionService")
public class PermissionServiceImpl implements PermissionService {


    @Resource
    private PermissionDao permissionDao;
    @Resource
    private PermissionRequestDao permissionRequestDao;
    @Resource
    private RequestMappingDao requestMappingDao;
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
            throw new BaseException(ECode.E_100060);
        }
        if (permissionDao.countByCode(ro.getCode()) > 0) {
            throw new BaseException(ECode.E_100061);
        }
        PermissionPO permissionPO = new PermissionPO();
        permissionPO.setName(ro.getName());
        permissionPO.setCode(ro.getCode());
        permissionPO.setState(AdminStatusEnum.Constants.ENABLED);
        permissionPO.setCreatedAt(new Date());
        permissionDao.save(permissionPO);
        // 添加关联的request_mapping
        if (ro.getRequestIds() != null) {
            ro.getRequestIds().forEach(id -> {
                PermissionRequestPO mapping = new PermissionRequestPO();
                mapping.setPermissionId(permissionPO.getId());
                mapping.setRequestId(id);
                mapping.setCreatedAt(new Date());
                permissionRequestDao.save(mapping);
            });
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void modify(Long id, ModifyPermissionRO ro) {
        if (permissionDao.countByIdNotAndName(id, ro.getName()) > 0) {
            throw new BaseException(ECode.E_100060);
        }
        if (permissionDao.countByIdNotAndCode(id, ro.getCode()) > 0) {
            throw new BaseException(ECode.E_100061);
        }
        PermissionPO po =
                permissionDao.findById(id).orElseThrow(() -> new BaseException(ECode.E_100062));
        po.setName(ro.getName());
        po.setCode(ro.getCode());
        permissionDao.save(po);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        PermissionPO po =
                permissionDao.findById(id).orElseThrow(() -> new BaseException(ECode.E_100062));
        permissionDao.delete(po);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void changeStatus(Long id, Integer status) {
        PermissionPO po =
                permissionDao.findById(id).orElseThrow(() -> new BaseException(ECode.E_100062));
        if (status.equals(po.getState())) {
            throw new BaseException(ECode.E_100063);
        }
        po.setState(status);
        permissionDao.save(po);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void configRequest(Long id, MapRequestRO ro) {
        if (CollectionUtils.isEmpty(ro.getRequestIds())) {
            // 如果是空，则直接删除关联的request_mapping记录即可
            permissionRequestDao.deleteByPermissionId(id);
        } else {
            final List<PermissionRequestPO> needDelete =
                    permissionRequestDao.findByPermissionId(id);
            final List<PermissionRequestPO> needInsert = new ArrayList<>(ro.getRequestIds().size());
            for (Long requestId : ro.getRequestIds()) {
                if (!containRequestId(needDelete, requestId)) {
                    PermissionRequestPO requestPO = new PermissionRequestPO();
                    requestPO.setPermissionId(id);
                    requestPO.setRequestId(requestId);
                    requestPO.setCreatedAt(new Date());
                    needInsert.add(requestPO);
                }
            }
            // 删除映射信息
            permissionRequestDao.deleteAll(needDelete);
            // 插入映射信息
            permissionRequestDao.saveAll(needInsert);
        }
    }

    @Override
    public List<RequestMappingVO> getAllRequest() {
        List<RequestMappingPO> requestMappingPOS = requestMappingDao.findByIsLost(0);
        return requestMappingPOS.stream()
                .sorted(Comparator.comparing(RequestMappingPO::getCreatedAt))
                .map(requestMappingPO -> modelMapper.map(requestMappingPO, RequestMappingVO.class))
                .collect(Collectors.toList());
    }

    @Override
    public RequestIdVO getConfigRequest(Long permissionId) {
        List<PermissionRequestPO> requestPOS =
                permissionRequestDao.findByPermissionId(permissionId);
        List<String> requestIds = requestPOS.stream()
                .map(permissionRequestPO -> permissionRequestPO.getRequestId().toString())
                .collect(Collectors.toList());
        RequestIdVO requestIdVO = new RequestIdVO();
        requestIdVO.setRequestIds(requestIds);
        return requestIdVO;
    }

    private boolean containRequestId(final List<PermissionRequestPO> permissionRequestPOS,
            Long requestId) {
        Iterator<PermissionRequestPO> iterator = permissionRequestPOS.iterator();
        while (iterator.hasNext()) {
            PermissionRequestPO next = iterator.next();
            if (next.getRequestId().equals(requestId)) {
                iterator.remove();
                return true;
            }
        }
        return false;
    }
}
