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

package org.gnrd.lam.ro.admin.user;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Setter
@Getter
public class AddUserRO {

    /**
     * 用户名
     *
     * @mock admin
     */
    @NotEmpty(message = "用户名不可为空")
    private String name;

    /**
     * 登录密码
     *
     * @mock 123456
     */
    @NotEmpty(message = "登录密码不可为空")
    private String password;

    /**
     * 手机号
     *
     * @mock 15500100020
     */
    @NotEmpty(message = "手机号不可为空")
    private String phone;
}
