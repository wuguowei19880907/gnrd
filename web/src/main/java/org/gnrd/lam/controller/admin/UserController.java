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
import org.gnrd.lam.ro.admin.user.AddUserRO;
import org.gnrd.lam.ro.admin.user.MapRoleRO;
import org.gnrd.lam.ro.admin.user.ModifyUserRO;
import org.gnrd.lam.ro.admin.user.ResetPasswordRO;
import org.gnrd.lam.service.admin.UserService;
import org.gnrd.lam.vo.admin.user.RoleIInUserVO;
import org.gnrd.lam.vo.admin.user.RoleIdVO;
import org.gnrd.lam.vo.admin.user.UserItemVO;
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
import java.util.List;

@RestController
@RequestMapping(value = "df-admin/users")
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 后台用户列表
     * 
     * @author wuguowei
     * @param nameQuery 用户名或者手机号，支持模糊查询|155
     * @param state 用户状态，0：禁用；1：激活|1
     */
    @GetMapping(name = "查询后台用户列表", value = "")
    public CommonResult<ResultPager<UserItemVO>> list(String nameQuery, Integer state,
            ParamPager pager) throws Exception {
        ResultPager<UserItemVO> list = userService.list(nameQuery, state, pager);
        return new CommonResult<>(list);
    }

    /**
     * 新增后台用户
     */
    @PostMapping(name = "新增后台用户", value = "")
    public VoidResult addUser(@RequestBody @Validated AddUserRO ro) throws Exception {
        userService.addUser(ro);
        return new VoidResult();
    }

    /**
     * 修改后台用户
     * 
     * @param id 后台用户id|10010
     */
    @PutMapping(name = "修改后台用户", value = "{id}")
    public VoidResult modifyUser(@PathVariable(name = "id") Long id,
            @RequestBody @Validated ModifyUserRO ro) throws Exception {
        userService.modifyUser(id, ro);
        return new VoidResult();
    }

    /**
     * 重置后台用户登录密码
     * 
     * @param id 后台用户id|10010
     */
    @PutMapping(name = "重置后台用户登录密码", value = "{id}/reset-password")
    public VoidResult resetPassword(@PathVariable(name = "id") Long id,
            @RequestBody @Validated ResetPasswordRO ro) throws Exception {
        userService.resetPassword(id, ro);
        return new VoidResult();
    }

    /**
     * 删除后台用户
     * 
     * @param id 后台用户id|10010
     */
    @DeleteMapping(name = "删除后台用户", value = "{id}")
    public VoidResult deleteUser(@PathVariable(name = "id") Long id) throws Exception {
        userService.deleteUser(id);
        return new VoidResult();
    }

    /**
     * 启用后台用户
     * 
     * @param id 后台用户id|10010
     */
    @PutMapping(name = "启用后台用户", value = "{id}/enable")
    public VoidResult enable(@PathVariable(name = "id") Long id) throws Exception {
        userService.enableUser(id);
        return new VoidResult();
    }

    /**
     * 禁用后台用户
     * 
     * @param id 后台用户id|10010
     */
    @PutMapping(name = "禁用后台用户", value = "{id}/disable")
    public VoidResult disable(@PathVariable(name = "id") Long id) throws Exception {
        userService.disableUser(id);
        return new VoidResult();
    }

    /**
     * 后台用户管理中获取所有用户角色
     */
    @GetMapping(name = "后台用户管理中获取所有用户角色", value = "roles")
    public CommonResult<List<RoleIInUserVO>> roles() throws Exception {
        List<RoleIInUserVO> list = userService.getActiveRoles();
        return new CommonResult<>(list);
    }

    /**
     * 为后台用户设置角色
     *
     * @param id 用户id|10010
     */
    @PutMapping(name = "为后台用户设置角色", value = "{id}/roles")
    public VoidResult bindRoles(@PathVariable(name = "id") Long id,
            @RequestBody @Validated MapRoleRO ro) throws Exception {
        userService.bindRoles(id, ro);
        return new VoidResult();
    }

    /**
     * 查看用户已配置的角色
     *
     * @param id 用户id|10010
     */
    @GetMapping(name = "查看用户已配置的角色", value = "{id}/roles")
    public CommonResult<RoleIdVO> getConfigPermissions(@PathVariable(name = "id") Long id)
            throws Exception {
        RoleIdVO vo = userService.getConfiguredRoleIds(id);
        return new CommonResult<>(vo);
    }
}
