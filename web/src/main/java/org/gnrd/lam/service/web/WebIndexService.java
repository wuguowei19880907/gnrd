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
import org.gnrd.lam.dao.UserDao;
import org.gnrd.lam.entity.UserPO;
import org.gnrd.lam.service.IndexService;
import org.gnrd.lam.vo.LoginVO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

@Component("indexService")
@Slf4j
public class WebIndexService implements IndexService {

	@Resource
	private UserDao userDao;
	@Resource
	private RSAUtil rsaUtil;

	@Override
	public LoginVO login(String username, String password, HttpServletRequest request) {
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
		HttpSession session = request.getSession();
		session.setAttribute("username", plainUsername);
		return null;
	}
}
