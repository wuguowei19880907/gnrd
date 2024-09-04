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

package org.gnrd.lam.controller.admin;

import org.gnrd.lam.common.result.CommonResult;
import org.gnrd.lam.common.result.ParamPager;
import org.gnrd.lam.common.result.ResultPager;
import org.gnrd.lam.common.result.VoidResult;
import org.gnrd.lam.ro.admin.AddUserRO;
import org.gnrd.lam.ro.admin.ModifyUserRO;
import org.gnrd.lam.ro.admin.ResetPasswordRO;
import org.gnrd.lam.service.admin.UserService;
import org.gnrd.lam.vo.admin.UserItemVO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping(value = "df-admin/users", name = "df.admin.users")
public class UserController {

	@Resource
	private UserService userService;

	/**
	 * 后台用户列表
	 * 
	 * @author wuguowei
	 * @param nameQuery
	 *            用户名或者手机号，支持模糊查询|155
	 * @param state
	 *            用户状态，0：禁用；1：激活|1
	 */
	@GetMapping(name = "list", value = "")
	public CommonResult<ResultPager<UserItemVO>> list(String nameQuery, Integer state, ParamPager pager)
			throws Exception {
		ResultPager<UserItemVO> list = userService.list(nameQuery, state, pager);
		return new CommonResult<>(list);
	}

	/**
	 * 新增后台用户
	 */
	@PostMapping(name = "addUser", value = "")
	public VoidResult addUser(@RequestBody @Validated AddUserRO ro) throws Exception {
		userService.addUser(ro);
		return new VoidResult();
	}

	/**
	 * 修改后台用户
	 * 
	 * @param id
	 *            后台用户id|10010
	 */
	@PutMapping(name = "modifyUser", value = "{id}")
	public VoidResult modifyUser(@PathVariable(name = "id") Long id, @RequestBody @Validated ModifyUserRO ro)
			throws Exception {
		userService.modifyUser(id, ro);
		return new VoidResult();
	}

	/**
	 * 重置后台用户登录密码
	 * 
	 * @param id
	 *            后台用户id|10010
	 */
	@PutMapping(name = "resetPassword", value = "{id}/reset-password")
	public VoidResult resetPassword(@PathVariable(name = "id") Long id, @RequestBody @Validated ResetPasswordRO ro)
			throws Exception {
		userService.resetPassword(id, ro);
		return new VoidResult();
	}

	/**
	 * 删除后台用户
	 * 
	 * @param id
	 *            后台用户id|10010
	 */
	@DeleteMapping(name = "deleteUser", value = "{id}")
	public VoidResult deleteUser(@PathVariable(name = "id") Long id) throws Exception {
		userService.deleteUser(id);
		return new VoidResult();
	}

	/**
	 * 启用后台用户
	 * 
	 * @param id
	 *            后台用户id|10010
	 */
	@PutMapping(name = "enableUser", value = "{id}/enable")
	public VoidResult enable(@PathVariable(name = "id") Long id) throws Exception {
		userService.enableUser(id);
		return new VoidResult();
	}

	/**
	 * 禁用后台用户
	 * 
	 * @param id
	 *            后台用户id|10010
	 */
	@PutMapping(name = "disableUser", value = "{id}/disable")
	public VoidResult disable(@PathVariable(name = "id") Long id) throws Exception {
		userService.disableUser(id);
		return new VoidResult();
	}
}
