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

package org.gnrd.lam.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Entity
@Table(name = "c_user")
@Setter
@Getter
public class UserPO extends BasePO {

    /**
     * 用户名称
     */
    @Column(name = "c_name")
    private String name;

    /**
     * 手机号
     */
    @Column(name = "c_phone")
    private String phone;

    /**
     * 登录密码
     */
    @Column(name = "c_password")
    private String password;

    /**
     * 超级管理员 0-不是 1-是
     */
    @Column(name = "is_super")
    private Integer superAdmin;

    /**
     * 状态 0-禁用 1-启用
     */
    @Column(name = "c_state")
    private Integer state;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof UserPO))
            return false;

        UserPO entity = (UserPO) o;

        return id.equals(entity.getId());
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
