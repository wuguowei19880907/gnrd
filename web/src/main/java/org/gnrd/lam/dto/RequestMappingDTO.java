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

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RequestMappingDTO {

    /**
     * RequestMapping.name
     */
    private String name;

    /**
     * RequestMapping.path
     */
    private String path;

    /**
     * RequestMapping.method
     */
    private String method;

    /**
     * RequestMapping.params
     */
    private String params;

    /**
     * RequestMapping.headers
     */
    private String headers;

    /**
     * RequestMapping.consumes
     */
    private String consumes;

    /**
     * RequestMapping.produces
     */
    private String produces;

    @Override
    public boolean equals(Object object) {
        if (this == object)
            return true;
        if (!(object instanceof RequestMappingDTO))
            return false;

        RequestMappingDTO that = (RequestMappingDTO) object;

        if (getName() != null ? !getName().equals(that.getName()) : that.getName() != null)
            return false;
        if (getPath() != null ? !getPath().equals(that.getPath()) : that.getPath() != null)
            return false;
        if (getMethod() != null ? !getMethod().equals(that.getMethod()) : that.getMethod() != null)
            return false;
        if (getParams() != null ? !getParams().equals(that.getParams()) : that.getParams() != null)
            return false;
        if (getHeaders() != null ? !getHeaders().equals(that.getHeaders())
                : that.getHeaders() != null)
            return false;
        if (getConsumes() != null ? !getConsumes().equals(that.getConsumes())
                : that.getConsumes() != null)
            return false;
        return getProduces() != null ? getProduces().equals(that.getProduces())
                : that.getProduces() == null;
    }

    @Override
    public int hashCode() {
        int result = getName() != null ? getName().hashCode() : 0;
        result = 31 * result + (getPath() != null ? getPath().hashCode() : 0);
        result = 31 * result + (getMethod() != null ? getMethod().hashCode() : 0);
        result = 31 * result + (getParams() != null ? getParams().hashCode() : 0);
        result = 31 * result + (getHeaders() != null ? getHeaders().hashCode() : 0);
        result = 31 * result + (getConsumes() != null ? getConsumes().hashCode() : 0);
        result = 31 * result + (getProduces() != null ? getProduces().hashCode() : 0);
        return result;
    }
}
