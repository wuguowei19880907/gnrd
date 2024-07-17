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

package org.gnrd.lam.controller;

import org.gnrd.lam.common.result.CommonResult;
import org.gnrd.lam.ro.LoginRO;
import org.gnrd.lam.service.IndexService;
import org.gnrd.lam.vo.LoginVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("api")
public class IndexController {

	@Resource
	private IndexService indexService;

	@GetMapping(value = "index")
	public String index() {
		return "ok";
	}

	@PostMapping(value = "auth/login")
	public CommonResult<LoginVO> auth(@RequestBody LoginRO loginRO, HttpServletRequest request) throws Exception {
		indexService.login(loginRO.getUsername(), loginRO.getPassword(), request);
		String id = request.getSession().getId();
		return new CommonResult<>(new LoginVO(id));
	}

	@GetMapping(value = "me")
	public CommonResult<String> getInfo(HttpServletRequest request) throws Exception {
		HttpSession session = request.getSession();
		Object username = session.getAttribute("username");
		return new CommonResult<>(username.toString());
	}

	@PostMapping(value = "auth/logout")
	public CommonResult<String> logout(HttpServletRequest request) throws Exception {
		HttpSession session = request.getSession();
		session.invalidate();
		return new CommonResult<>("logout ok");
	}
}
