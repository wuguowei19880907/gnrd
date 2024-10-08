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

package org.gnrd.lam.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LoginPermissionDTO implements Serializable {

    /**
     * 权限id
     */
    private Long id;

    /**
     * 权限名称
     */
    private String name;

    /**
     * 权限编码
     */
    private String code;

    @Override
    public boolean equals(Object object) {
        if (this == object)
            return true;
        if (!(object instanceof LoginPermissionDTO))
            return false;

        LoginPermissionDTO that = (LoginPermissionDTO) object;

        if (getName() != null ? !getName().equals(that.getName()) : that.getName() != null)
            return false;
        return getCode() != null ? getCode().equals(that.getCode()) : that.getCode() == null;
    }

    @Override
    public int hashCode() {
        int result = getName() != null ? getName().hashCode() : 0;
        result = 31 * result + (getCode() != null ? getCode().hashCode() : 0);
        return result;
    }
}
