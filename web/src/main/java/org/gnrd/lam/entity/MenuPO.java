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
@Table(name = "df_menu")
@Setter
@Getter
public class MenuPO extends BasePO {

    /**
     * 菜单名称
     */
    @Column(name = "df_name")
    private String name;

    /**
     * 菜单编码
     */
    @Column(name = "df_code")
    private String code;

    /**
     * 菜单路径
     */
    @Column(name = "df_path")
    private String path;

    /**
     * 排序，值越大，菜单越靠上
     */
    @Column(name = "df_sort")
    private Integer sort;

    /**
     * 状态 0-禁用 1-启用
     */
    @Column(name = "df_status")
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
        if (!(o instanceof MenuPO))
            return false;

        MenuPO entity = (MenuPO) o;

        return id.equals(entity.getId());
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
