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

package org.gnrd.lam.common.tools;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.gnrd.lam.dao.RequestMappingDao;
import org.gnrd.lam.entity.RequestMappingPO;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class ControllerLogger implements ApplicationListener<ApplicationReadyEvent> {

    @Resource
    private RequestMappingHandlerMapping requestMappingHandlerMapping;

    @Resource
    private RequestMappingDao requestMappingDao;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        Map<RequestMappingInfo, HandlerMethod> handlerMethods =
                requestMappingHandlerMapping.getHandlerMethods();
        final List<RequestMappingPO> requestMappingsInDB = requestMappingDao.findByIsLost(0);
        final List<RequestMappingPO> requestMappings =
                new ArrayList<>(handlerMethods.entrySet().size());
        for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : handlerMethods.entrySet()) {
            RequestMappingInfo mappingInfo = entry.getKey();

            String name = mappingInfo.getName();
            String url = mappingInfo.getPatternsCondition() != null
                    ? mappingInfo.getPatternsCondition().toString()
                    : mappingInfo.getPathPatternsCondition().toString();
            String method = mappingInfo.getMethodsCondition().toString();
            String params = mappingInfo.getParamsCondition().toString();
            String header = mappingInfo.getHeadersCondition().toString();
            String consume = mappingInfo.getConsumesCondition().toString();
            String produce = mappingInfo.getProducesCondition().toString();
            RequestMappingPO po = new RequestMappingPO();
            po.setName(name);
            po.setPath(url);
            po.setMethod(method);
            po.setHeaders(header);
            po.setConsumes(consume);
            po.setParams(params);
            po.setProduces(produce);
            log.debug(
                    "Controller Name : {}, Method: {}, URL: {}, params: {}, header: {}, consume: {}, produce: {}",
                    name, method, url, params, header, consume, produce);
            if (!contains(requestMappingsInDB, po)) {
                requestMappings.add(po);
            }
        }
        final Date now = new Date();
        // requestMappingsInDB剩余的标记为丢失
        for (RequestMappingPO requestMappingPO : requestMappingsInDB) {
            requestMappingPO.setIsLost(1);
            requestMappingPO.setUpdatedAt(now);
        }
        requestMappingDao.saveAll(requestMappingsInDB);
        // requestMappings的数据需要插入到数据库
        for (RequestMappingPO requestMapping : requestMappings) {
            requestMapping.setIsLost(0);
            requestMapping.setCreatedAt(now);
        }
        requestMappingDao.saveAll(requestMappings);
    }

    private boolean contains(final List<RequestMappingPO> list, RequestMappingPO po) {
        Iterator<RequestMappingPO> iterator = list.iterator();
        while (iterator.hasNext()) {
            RequestMappingPO item = iterator.next();
            // 判断，如果po的数据完全能存在于list，则将list中的该元素清楚
            if (StringUtils.equals(item.getName(), po.getName())
                    && StringUtils.equals(item.getPath(), po.getPath())
                    && StringUtils.equals(item.getMethod(), po.getMethod())
                    && StringUtils.equals(item.getHeaders(), po.getHeaders())
                    && StringUtils.equals(item.getConsumes(), po.getConsumes())
                    && StringUtils.equals(item.getParams(), po.getParams())
                    && StringUtils.equals(item.getProduces(), po.getProduces())) {
                iterator.remove();
                return true;
            }
        }
        return false;
    }
}
