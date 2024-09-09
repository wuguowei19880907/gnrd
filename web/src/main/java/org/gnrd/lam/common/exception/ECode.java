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

package org.gnrd.lam.common.exception;

import lombok.Getter;

@Getter
public enum ECode {

    E_000001("000001", "用户名或密码错误"),
    E_100001("100001", "请先登录"),
    E_100002("100002", "不允许访问"),

    // 后台用户管理
    E_100050("100050", "用户名已存在"),
    E_100051("100051", "手机号已存在"),
    E_100052("100052", "用户信息不存在"),
    E_100053("100053", "用户状态不正确"),

    // 权限管理
    E_100060("100060", "权限名已存在"),
    E_100061("100061", "权限编码已存在"),
    E_100062("100062", "权限信息不存在"),
    E_100063("100063", "权限状态不正确"),

    // 角色管理
    E_100070("100070", "角色名已存在"),
    E_100071("100071", "角色编码已存在"),
    E_100072("100072", "角色信息不存在"),
    E_100073("100073", "角色状态不正确"),

    // 通用
    E_999998("999998", "参数检验异常"),
    E_999999("999999", "业务异常");

    private String code;

    private String message;

    ECode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
