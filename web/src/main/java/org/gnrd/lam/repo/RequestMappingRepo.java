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

package org.gnrd.lam.repo;

import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.querydsl.BlazeJPAQuery;
import com.querydsl.core.types.Projections;
import org.gnrd.lam.dto.RequestPermissionDTO;
import org.gnrd.lam.entity.QPermissionRequestPO;
import org.gnrd.lam.entity.QRequestMappingPO;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import java.util.List;

@Repository("requestMappingRepo")
public class RequestMappingRepo {

    @Resource
    private CriteriaBuilderFactory cbf;

    @Resource
    private EntityManager entityManager;

    public List<RequestPermissionDTO> getRequestPermissions(final String requestName) {
        QPermissionRequestPO qPermissionRequestPO = QPermissionRequestPO.permissionRequestPO;
        QRequestMappingPO qRequestMappingPO = QRequestMappingPO.requestMappingPO;
        BlazeJPAQuery<RequestPermissionDTO> query = new BlazeJPAQuery<>(entityManager, cbf)
                .from(qRequestMappingPO)
                .select(Projections.constructor(RequestPermissionDTO.class, qRequestMappingPO.name,
                        qPermissionRequestPO.permissionId))
                .leftJoin(qPermissionRequestPO)
                .on(qRequestMappingPO.id.eq(qPermissionRequestPO.requestId));
        if (StringUtils.hasLength(requestName)) {
            query.where(qRequestMappingPO.name.eq(requestName));
        }
        return query.fetch();
    }
}
