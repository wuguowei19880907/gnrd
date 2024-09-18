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

package org.gnrd.lam.service.web;

import lombok.extern.slf4j.Slf4j;
import org.gnrd.lam.common.constants.AdminStatusEnum;
import org.gnrd.lam.common.constants.CacheKeys;
import org.gnrd.lam.common.encrypt.RSAUtil;
import org.gnrd.lam.common.encrypt.password.PasswordEncoder;
import org.gnrd.lam.common.exception.BaseException;
import org.gnrd.lam.common.exception.ECode;
import org.gnrd.lam.common.result.ParamPager;
import org.gnrd.lam.common.tools.SessionUtils;
import org.gnrd.lam.configuration.AppProperties;
import org.gnrd.lam.dao.PermissionRequestDao;
import org.gnrd.lam.dao.RequestMappingDao;
import org.gnrd.lam.dao.UserDao;
import org.gnrd.lam.dto.LoginMenuDTO;
import org.gnrd.lam.dto.LoginPermissionDTO;
import org.gnrd.lam.dto.LoginUserDTO;
import org.gnrd.lam.dto.RequestMappingDTO;
import org.gnrd.lam.entity.UserPO;
import org.gnrd.lam.manager.DFCacheManage;
import org.gnrd.lam.service.IndexService;
import org.gnrd.lam.vo.CommonLoginVO;
import org.gnrd.lam.vo.MenuVO;
import org.modelmapper.ModelMapper;
import org.springframework.data.util.CastUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.net.URLEncoder;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component("indexService")
@Slf4j
public class WebIndexService implements IndexService {

    private final String REDIRECT_URL = "%s?%s=%s";

    @Resource
    private UserDao userDao;
    @Resource
    private DFCacheManage dfCacheManage;
    @Resource
    private PermissionRequestDao permissionRequestDao;
    @Resource
    private RequestMappingDao requestMappingDao;
    @Resource
    private RSAUtil rsaUtil;
    @Resource
    private ModelMapper modelMapper;
    @Resource
    private AppProperties appProperties;
    @Resource
    private SessionUtils sessionUtil;
    @Resource
    private PasswordEncoder passwordEncoder;

    private final LoginPermissionDTO SUPER_PERMISSION =
            new LoginPermissionDTO(1L, "超级管理员", "super");

    @Override
    public ModelAndView login(String username, String password, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        return null;
    }

    @Override
    public CommonLoginVO login(String username, String password, HttpServletRequest request)
            throws Exception {
        final String plainUsername;
        final String plainPassword;
        try {
            plainUsername = rsaUtil.decrypt(username);
            plainPassword = rsaUtil.decrypt(password);
        } catch (Exception e) {
            log.error("用户名或密码不能正确解密", e);
            throw new BaseException(ECode.E_000001);
        }
        Optional<UserPO> byName = userDao.findByName(plainUsername);
        UserPO user = byName.orElseThrow(() -> {
            log.error("用户名不存在");
            return new BaseException(ECode.E_000001);
        });
        if (!passwordEncoder.matches(plainPassword, user.getPassword())) {
            log.debug("密码错误");
            throw new BaseException(ECode.E_000001);
        }
        if (user.getState() == AdminStatusEnum.Constants.DISABLED) {
            throw new BaseException(ECode.E_100003);
        }

        dfCacheManage.getLoginUserMenus(user.getId());
        List<LoginPermissionDTO> permissionDTOS =
                dfCacheManage.getLoginUserPermissions(user.getId());
        List<RequestMappingDTO> requestMappings = getRequestMappings(permissionDTOS);
        dfCacheManage.putLoginUserRequestMappings(user.getId(), requestMappings);
        LoginUserDTO loginUserDTO = modelMapper.map(user, LoginUserDTO.class);
        dfCacheManage.putLoginUser(user.getId(), loginUserDTO);
        HttpSession httpSession = request.getSession(true);
        String uuid = httpSession.getId();
        httpSession.setAttribute(CacheKeys.SK_LOGIN_USERID, user.getId());
        httpSession.setAttribute(CacheKeys.SK_LOGIN_USERNAME, user.getName());
        if (user.getSuperAdmin().equals(1)) {
            return new CommonLoginVO(true, uuid);
        } else {
            String url = String.format(REDIRECT_URL, appProperties.getIndexUrl(), "df-auth-id",
                    URLEncoder.encode(uuid, "UTF-8"));
            return new CommonLoginVO(false, url);
        }
    }

    @Override
    public String index(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        List<LoginPermissionDTO> list = CastUtils.cast(session.getAttribute("login-permissions"));
        response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
        if (list.contains(SUPER_PERMISSION)) {
            return "super/dashboard";
        } else {
            return appProperties.getIndexUrl();
        }
    }

    @Override
    public ModelAndView getAdminUsers(String username, ParamPager pager) {

        return null;
    }

    @Override
    public List<MenuVO> getMe(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Long userId = (Long) session.getAttribute(CacheKeys.SK_LOGIN_USERID);
        List<LoginMenuDTO> loginUserMenus = dfCacheManage.getLoginUserMenus(userId);
        return loginUserMenus
                .stream().map(loginMenuDTO -> new MenuVO(loginMenuDTO.getName(),
                        loginMenuDTO.getCode(), loginMenuDTO.getPath()))
                .collect(Collectors.toList());
    }

    private List<RequestMappingDTO> getRequestMappings(List<LoginPermissionDTO> permissionDTOS) {
        List<Long> permissionIds =
                permissionDTOS.stream().map(LoginPermissionDTO::getId).collect(Collectors.toList());
        return permissionRequestDao.getRequestMappingDTO(permissionIds);
    }
}
