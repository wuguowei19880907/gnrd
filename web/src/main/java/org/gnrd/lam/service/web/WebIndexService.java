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
import org.gnrd.lam.common.encrypt.RSAUtil;
import org.gnrd.lam.common.exception.BaseException;
import org.gnrd.lam.common.exception.ECode;
import org.gnrd.lam.common.tools.SessionUtils;
import org.gnrd.lam.configuration.AppProperties;
import org.gnrd.lam.dao.UserDao;
import org.gnrd.lam.dao.UserRoleDao;
import org.gnrd.lam.dto.LoginPermissionDTO;
import org.gnrd.lam.dto.LoginUserDTO;
import org.gnrd.lam.entity.PermissionPO;
import org.gnrd.lam.entity.UserPO;
import org.gnrd.lam.service.IndexService;
import org.modelmapper.ModelMapper;
import org.springframework.data.util.CastUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.net.URLEncoder;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component("indexService")
@Slf4j
public class WebIndexService implements IndexService {

	private final String REDIRECT_URL = "redirect:%s?%s=%s";

	@Resource
	private UserDao userDao;
	@Resource
	private UserRoleDao userRoleDao;
	@Resource
	private RSAUtil rsaUtil;
	@Resource
	private ModelMapper modelMapper;
	@Resource
	private AppProperties appProperties;
	@Resource
	private SessionUtils sessionUtil;

	private final LoginPermissionDTO SUPER_PERMISSION = new LoginPermissionDTO("超级管理员", "super");

	@Override
	public ModelAndView login(String username, String password, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
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
		if (!plainPassword.equals(user.getPassword())) {
			log.debug("密码错误");
			throw new BaseException(ECode.E_000001);
		}
		Set<PermissionPO> permissionPOSet = userRoleDao.findByUserId(user.getId());
		List<LoginPermissionDTO> permissionDTOS = permissionPOSet.stream()
				.sorted(Comparator.comparing(PermissionPO::getId))
				.map(permissionPO -> modelMapper.map(permissionPO, LoginPermissionDTO.class))
				.collect(Collectors.toList());
		LoginUserDTO loginUserDTO = modelMapper.map(user, LoginUserDTO.class);
		loginUserDTO.setPermissions(permissionDTOS);
		String uuid = sessionUtil.getUUid();
		sessionUtil.storeInfo(uuid, loginUserDTO);
		if (permissionDTOS.contains(SUPER_PERMISSION)) {
			response.setHeader("X-Auth-Token", uuid);
			return new ModelAndView("dashboard");
		} else {
			String url = String.format(REDIRECT_URL, appProperties.getIndexUrl(), "df-auth-id",
					URLEncoder.encode(uuid, "UTF-8"));
			return new ModelAndView(url);
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

}
