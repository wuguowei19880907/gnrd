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

package org.gnrd.lam.service.admin;

import org.gnrd.lam.common.result.ParamPager;
import org.gnrd.lam.common.result.ResultPager;
import org.gnrd.lam.ro.admin.role.AddRoleRO;
import org.gnrd.lam.ro.admin.role.MapPermissionRO;
import org.gnrd.lam.ro.admin.role.ModifyRoleRO;
import org.gnrd.lam.vo.admin.role.PermissionIdVO;
import org.gnrd.lam.vo.admin.role.RoleItemVO;
import org.gnrd.lam.vo.admin.role.RolePermissionVO;

import java.util.List;

public interface RoleService {

    ResultPager<RoleItemVO> list(String nameQuery, Integer state, ParamPager pager);

    void add(AddRoleRO ro);

    void modify(Long id, ModifyRoleRO ro);

    void delete(Long id);

    void changeStatus(Long id, Integer status);

    void configPermissions(Long id, MapPermissionRO ro);

    List<RolePermissionVO> getPermissions();

    PermissionIdVO getPermissionIds(Long roleId);
}
