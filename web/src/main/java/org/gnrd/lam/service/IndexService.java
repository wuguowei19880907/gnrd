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

package org.gnrd.lam.service;

import org.gnrd.lam.common.result.ParamPager;
import org.gnrd.lam.vo.CommonLoginVO;
import org.gnrd.lam.vo.MenuVO;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface IndexService {

    ModelAndView login(String username, String password, HttpServletRequest request,
            HttpServletResponse response) throws Exception;

    CommonLoginVO login(String username, String password) throws Exception;

    String index(HttpServletRequest request, HttpServletResponse response);

    ModelAndView getAdminUsers(String username, ParamPager pager);

    List<MenuVO> getMe(String token);
}
