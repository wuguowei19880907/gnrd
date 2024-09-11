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

package org.gnrd.lam.vo.admin.permission;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * request_mapping_id 数据
 *
 * @author WUGUOWEI 2024年08月28日
 */
@Setter
@Getter
public class RequestIdVO {

    /**
     * request_mapping id
     *
     * @mock 100010
     */
    private List<String> requestIds;
}
