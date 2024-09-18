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

package org.gnrd.lam.aop.auth;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.gnrd.lam.common.constants.AdminStatusEnum;
import org.gnrd.lam.common.constants.CacheKeys;
import org.gnrd.lam.common.exception.BaseException;
import org.gnrd.lam.common.exception.ECode;
import org.gnrd.lam.common.tools.Convert;
import org.gnrd.lam.dto.LoginUserDTO;
import org.gnrd.lam.dto.RequestMappingDTO;
import org.gnrd.lam.manager.DFCacheManage;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;

@Aspect
@Component
@Slf4j
public class AuthorizeAspect {

    @Resource
    private DFCacheManage dfCacheManage;

    @Pointcut("@annotation(requestMapping)")
    public void point(RequestMapping requestMapping) {}

    @Pointcut("@annotation(superAdmin)")
    public void point0(SuperAdmin superAdmin) {}

    @Before(value = "point(requestMapping)", argNames = "joinPoint,requestMapping")
    public void successLog(JoinPoint joinPoint, RequestMapping requestMapping) {

        // 对于RequestMapping未设置name的接口，不做校验，直接跳过
        if (org.springframework.util.StringUtils.hasLength(requestMapping.name())) {
            // 获取当前登录用户的userId
            HttpSession session = Convert.currentRequest().getSession(false);
            Long userId = (Long) session.getAttribute(CacheKeys.SK_LOGIN_USERID);
            LoginUserDTO loginUser = dfCacheManage.getLoginUser(userId);
            // 如果用户被禁用，则不允许访问任何接口
            if (loginUser.getState() == AdminStatusEnum.Constants.DISABLED) {
                throw new BaseException(HttpStatus.BAD_REQUEST, ECode.E_100004);
            }
            // 根据userId，获取当前用户所包含的RequestMapping信息
            List<RequestMappingDTO> loginUserRequestMappings =
                    dfCacheManage.getLoginUserRequestMappings(userId);
            for (RequestMappingDTO loginUserRequestMapping : loginUserRequestMappings) {
                // 如果当前的RequestMapping name等于用户绑定的。说明用户有权限访问改接口
                if (requestMapping.name().equals(loginUserRequestMapping.getName())) {
                    return;
                }
            }
            throw new BaseException(ECode.E_100002);
        }
    }

    /**
     * 超级管理员角色访问接口的权限校验
     */
    @Before(value = "point0(superAdmin)", argNames = "joinPoint,superAdmin")
    public void superAdminSuccess(JoinPoint joinPoint, SuperAdmin superAdmin) {
        HttpSession session = Convert.currentRequest().getSession(false);
        Long userId = (Long) session.getAttribute(CacheKeys.SK_LOGIN_USERID);
        LoginUserDTO loginUser = dfCacheManage.getLoginUser(userId);
        if (loginUser.getSuperAdmin() != 1) {
            throw new BaseException(ECode.E_100002);
        }
    }
}
