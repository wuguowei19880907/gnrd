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
import org.gnrd.lam.ro.admin.user.AddUserRO;
import org.gnrd.lam.ro.admin.user.MapRoleRO;
import org.gnrd.lam.ro.admin.user.ModifyUserRO;
import org.gnrd.lam.ro.admin.user.ResetPasswordRO;
import org.gnrd.lam.vo.admin.user.RoleIInUserVO;
import org.gnrd.lam.vo.admin.user.RoleIdVO;
import org.gnrd.lam.vo.admin.user.UserItemVO;

import java.util.List;

public interface UserService {

    ResultPager<UserItemVO> list(String nameQuery, Integer status, ParamPager pager);

    void addUser(AddUserRO ro);

    void modifyUser(Long id, ModifyUserRO ro);

    void resetPassword(Long id, ResetPasswordRO ro);

    void deleteUser(Long id);

    void disableUser(Long id);

    void enableUser(Long id);

    List<RoleIInUserVO> getActiveRoles();

    void bindRoles(Long id, MapRoleRO ro);

    RoleIdVO getConfiguredRoleIds(Long userId);
}
