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
import org.gnrd.lam.service.admin.PermissionService;
import org.gnrd.lam.vo.admin.PermissionItemVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping(value = "df-admin/permissions", name = "df.admin.permissions")
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
}
