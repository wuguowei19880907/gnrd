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
import org.gnrd.lam.ro.admin.permission.AddPermissionRO;
import org.gnrd.lam.ro.admin.permission.ChangeStatusRO;
import org.gnrd.lam.ro.admin.permission.MapRequestRO;
import org.gnrd.lam.ro.admin.permission.ModifyPermissionRO;
import org.gnrd.lam.service.admin.PermissionService;
import org.gnrd.lam.vo.admin.PermissionItemVO;
import org.gnrd.lam.vo.admin.RequestIdVO;
import org.gnrd.lam.vo.admin.RequestMappingVO;
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
@RequestMapping(value = "df-admin/permissions")
public class PermissionController {

    @Resource
    private PermissionService permissionService;

    /**
     * 角色信息列表
     *
     * @author wuguowei
     * @param nameQuery 角色名，支持模糊查询|后台经理
     * @param state 角色状态，0：禁用；1：激活|1
     */
    @GetMapping(name = "list", value = "")
    public CommonResult<ResultPager<PermissionItemVO>> list(String nameQuery, Integer state,
            ParamPager pager) throws Exception {
        ResultPager<PermissionItemVO> list = permissionService.list(nameQuery, state, pager);
        return new CommonResult<>(list);
    }

    /**
     * 新增用户权限
     */
    @PostMapping(name = "addPermission", value = "")
    public VoidResult addPermission(@RequestBody @Validated AddPermissionRO ro) throws Exception {
        permissionService.add(ro);
        return new VoidResult();
    }

    /**
     * 修改用户权限
     *
     * @param id 用户权限id|10010
     */
    @PutMapping(name = "modifyPermission", value = "{id}")
    public VoidResult modifyPermission(@PathVariable(name = "id") Long id,
            @RequestBody @Validated ModifyPermissionRO ro) throws Exception {
        permissionService.modify(id, ro);
        return new VoidResult();
    }

    /**
     * 删除用户权限
     *
     * @param id 用户权限id|10010
     */
    @DeleteMapping(name = "deletePermission", value = "{id}")
    public VoidResult deletePermission(@PathVariable(name = "id") Long id) throws Exception {
        permissionService.delete(id);
        return new VoidResult();
    }

    /**
     * 启用/禁用 用户权限
     *
     * @param id 用户权限id|10010
     */
    @PutMapping(name = "变更权限状态", value = "{id}/change-status")
    public VoidResult changeStatus(@PathVariable(name = "id") Long id,
            @RequestBody @Validated ChangeStatusRO ro) throws Exception {
        permissionService.changeStatus(id, ro.getState());
        return new VoidResult();
    }

    /**
     * RequestMapping列表
     *
     * @author wuguowei
     */
    @GetMapping(name = "allRequestMappings", value = "request-mappings")
    public CommonResult<List<RequestMappingVO>> allRequestMappings() throws Exception {
        List<RequestMappingVO> allRequest = permissionService.getAllRequest();
        return new CommonResult<>(allRequest);
    }

    /**
     * 为权限配置RequestMapping
     *
     * @param id 用户权限id|10010
     */
    @PutMapping(name = "configRequestMappings", value = "{id}/request-mappings")
    public VoidResult configRequestMappings(@PathVariable(name = "id") Long id,
            @RequestBody @Validated MapRequestRO ro) throws Exception {
        permissionService.configRequest(id, ro);
        return new VoidResult();
    }

    /**
     * 查看权限已配置的 RequestMapping
     *
     * @param id 用户权限id|10010
     */
    @GetMapping(name = "查看权限已配置的 RequestMapping", value = "{id}/request-mappings")
    public CommonResult<RequestIdVO> getRequestMappings(@PathVariable(name = "id") Long id)
            throws Exception {
        RequestIdVO configRequest = permissionService.getConfigRequest(id);
        return new CommonResult<>(configRequest);
    }
}
