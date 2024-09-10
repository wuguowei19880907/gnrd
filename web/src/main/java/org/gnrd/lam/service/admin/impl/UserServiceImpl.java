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
import org.gnrd.lam.common.encrypt.password.PasswordEncoder;
import org.gnrd.lam.common.exception.BaseException;
import org.gnrd.lam.common.exception.ECode;
import org.gnrd.lam.common.result.ParamPager;
import org.gnrd.lam.common.result.ResultPager;
import org.gnrd.lam.common.tools.Convert;
import org.gnrd.lam.dao.RoleDao;
import org.gnrd.lam.dao.UserDao;
import org.gnrd.lam.dao.UserRoleDao;
import org.gnrd.lam.dto.UserRoleLinkDTO;
import org.gnrd.lam.entity.UserPO;
import org.gnrd.lam.ro.admin.user.AddUserRO;
import org.gnrd.lam.ro.admin.user.ModifyUserRO;
import org.gnrd.lam.ro.admin.user.ResetPasswordRO;
import org.gnrd.lam.service.admin.UserService;
import org.gnrd.lam.vo.admin.RoleIInUserVO;
import org.gnrd.lam.vo.admin.UserItemVO;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component("userService")
public class UserServiceImpl implements UserService {

    @Resource
    private PasswordEncoder passwordEncoder;
    @Resource
    private UserDao userDao;
    @Resource
    private UserRoleDao userRoleDao;
    @Resource
    private RoleDao roleDao;
    @Resource
    private ModelMapper modelMapper;

    @Override
    public ResultPager<UserItemVO> list(String queryName, Integer status, ParamPager pager) {
        Specification<UserPO> specification = ((root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();
            if (StringUtils.hasText(queryName)) {
                Predicate pre1 = criteriaBuilder.like(root.get("name"),
                        Convert.getFuzzyQueryParam(queryName));
                Predicate pre2 = criteriaBuilder.like(root.get("phone"),
                        Convert.getFuzzyQueryParam(queryName));
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.or(pre1, pre2));
            }
            if (status != null) {
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.equal(root.get("state"), status));
            }
            return predicate;
        });
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(pager.getNumber(), pager.getSize(), sort);
        Page<UserPO> users = userDao.findAll(specification, pageable);
        List<UserPO> content = users.getContent();
        final List<UserItemVO> vos = new ArrayList<>(content.size());
        final List<Long> userIds = content.stream().map(UserPO::getId).collect(Collectors.toList());
        final List<UserRoleLinkDTO> userRoles = userRoleDao.findUserRoles(userIds);
        Map<Long, List<UserRoleLinkDTO>> userRoleMap =
                userRoles.stream().collect(Collectors.groupingBy(UserRoleLinkDTO::getUserId));
        for (UserPO userPO : content) {
            UserItemVO userItemVO = modelMapper.map(userPO, UserItemVO.class);
            List<UserRoleLinkDTO> linkDTOS = userRoleMap.get(userPO.getId());
            List<RoleIInUserVO> inUserVOS = linkDTOS.stream().map(userRoleLinkDTO -> {
                RoleIInUserVO vo = new RoleIInUserVO();
                vo.setId(userRoleLinkDTO.getRoleId().toString());
                vo.setName(userRoleLinkDTO.getRoleName());
                return vo;
            }).collect(Collectors.toList());
            userItemVO.setRoles(inUserVOS);
            vos.add(userItemVO);
        }
        Page<UserItemVO> map = new PageImpl<>(vos, users.getPageable(), users.getTotalElements());
        return ResultPager.of(map);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addUser(AddUserRO ro) {
        if (userDao.countByName(ro.getName()) > 0) {
            throw new BaseException(ECode.E_100050);
        }
        if (userDao.countByPhone(ro.getPhone()) > 0) {
            throw new BaseException(ECode.E_100051);
        }
        UserPO userPO = new UserPO();
        userPO.setName(ro.getName());
        userPO.setPassword(passwordEncoder.encode(ro.getPassword()));
        userPO.setState(AdminStatusEnum.Constants.ENABLED);
        userPO.setPhone(ro.getPhone());
        userPO.setSuperAdmin(0);
        userPO.setCreatedAt(new Date());
        userDao.save(userPO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void modifyUser(Long id, ModifyUserRO ro) {
        if (userDao.countByIdNotAndName(id, ro.getName()) > 0) {
            throw new BaseException(ECode.E_100050);
        }
        if (userDao.countByIdNotAndPhone(id, ro.getPhone()) > 0) {
            throw new BaseException(ECode.E_100051);
        }
        UserPO po = userDao.findById(id).orElseThrow(() -> new BaseException(ECode.E_100052));
        po.setName(ro.getName());
        po.setPhone(ro.getPhone());
        userDao.save(po);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void resetPassword(Long id, ResetPasswordRO ro) {
        UserPO po = userDao.findById(id).orElseThrow(() -> new BaseException(ECode.E_100052));
        po.setPassword(passwordEncoder.encode(ro.getPassword()));
        userDao.save(po);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteUser(Long id) {
        UserPO po = userDao.findById(id).orElseThrow(() -> new BaseException(ECode.E_100052));
        userDao.delete(po);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void disableUser(Long id) {
        UserPO po = userDao.findById(id).orElseThrow(() -> new BaseException(ECode.E_100052));
        if (AdminStatusEnum.DISABLED.getValue() == po.getState()) {
            throw new BaseException(ECode.E_100053);
        }
        po.setState(AdminStatusEnum.DISABLED.getValue());
        userDao.save(po);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void enableUser(Long id) {
        UserPO po = userDao.findById(id).orElseThrow(() -> new BaseException(ECode.E_100052));
        if (AdminStatusEnum.ENABLED.getValue() == po.getState()) {
            throw new BaseException(ECode.E_100053);
        }
        po.setState(AdminStatusEnum.ENABLED.getValue());
        userDao.save(po);
    }
}
