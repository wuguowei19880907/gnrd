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
import org.gnrd.lam.ro.admin.role.AddRoleRO;
import org.gnrd.lam.ro.admin.role.ChangeStatusRO;
import org.gnrd.lam.ro.admin.role.MapPermissionRO;
import org.gnrd.lam.ro.admin.role.ModifyRoleRO;
import org.gnrd.lam.service.admin.RoleService;
import org.gnrd.lam.vo.admin.PermissionIdVO;
import org.gnrd.lam.vo.admin.RoleItemVO;
import org.gnrd.lam.vo.admin.RolePermissionVO;
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
@RequestMapping(value = "df-admin/roles")
public class RoleController {

    @Resource
    private RoleService roleService;

    /**
     * 后台角色列表
     *
     * @author wuguowei
     * @param nameQuery 角色名或者角色编码，支持模糊查询|Common_User
     * @param state 角色状态，0：禁用；1：激活|1
     */
    @GetMapping(name = "查询后台角色列表", value = "")
    public CommonResult<ResultPager<RoleItemVO>> list(String nameQuery, Integer state, ParamPager pager) throws Exception {
        ResultPager<RoleItemVO> list = roleService.list(nameQuery, state, pager);
        return new CommonResult<>(list);
    }

    /**
     * 新增用户角色
     */
    @PostMapping(name = "新增用户角色", value = "")
    public VoidResult addRole(@RequestBody @Validated AddRoleRO ro) throws Exception {
        roleService.add(ro);
        return new VoidResult();
    }

    /**
     * 修改用户角色
     *
     * @param id 用户角色id|10010
     */
    @PutMapping(name = "修改用户角色", value = "{id}")
    public VoidResult modifyRole(@PathVariable(name = "id") Long id,
                                       @RequestBody @Validated ModifyRoleRO ro) throws Exception {
        roleService.modify(id, ro);
        return new VoidResult();
    }

    /**
     * 删除用户角色
     *
     * @param id 用户角色id|10010
     */
    @DeleteMapping(name = "删除用户角色", value = "{id}")
    public VoidResult deleteRole(@PathVariable(name = "id") Long id) throws Exception {
        roleService.delete(id);
        return new VoidResult();
    }

    /**
     * 用户角色的启用与禁用
     *
     * @param id 用户角色id|10010
     */
    @PutMapping(name = "用户角色的启用与禁用", value = "{id}/change-status")
    public VoidResult changeStatus(@PathVariable(name = "id") Long id,
                                   @RequestBody @Validated ChangeStatusRO ro) throws Exception {
        roleService.changeStatus(id, ro.getState());
        return new VoidResult();
    }

    /**
     * 角色管理获取权限列表
     *
     * @author wuguowei
     */
    @GetMapping(name = "角色管理获取权限列表", value = "permissions")
    public CommonResult<List<RolePermissionVO>> allPermissions() throws Exception {
        List<RolePermissionVO> rolePermissionVOS = roleService.getPermissions();
        return new CommonResult<>(rolePermissionVOS);
    }

    /**
     * 为角色配置权限
     *
     * @param id 用户角色id|10010
     */
    @PutMapping(name = "为角色配置权限", value = "{id}/permissions")
    public VoidResult configPermissions(@PathVariable(name = "id") Long id,
                                            @RequestBody @Validated MapPermissionRO ro) throws Exception {
        roleService.configPermissions(id, ro);
        return new VoidResult();
    }

    /**
     * 查看角色已经配置的权限
     *
     * @param id 用户角色id|10010
     */
    @GetMapping(name = "查看角色已经配置的权限", value = "{id}/permissions")
    public CommonResult<PermissionIdVO> getConfigPermissions(@PathVariable(name = "id") Long id)
            throws Exception {
        PermissionIdVO permissionIdVO = roleService.getPermissionIds(id);
        return new CommonResult<>(permissionIdVO);
    }
}
