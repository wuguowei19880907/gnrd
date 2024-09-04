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

package org.gnrd.lam.controller;

import org.gnrd.lam.common.constants.AuthToken;
import org.gnrd.lam.common.exception.BaseException;
import org.gnrd.lam.common.result.CommonResult;
import org.gnrd.lam.ro.LoginRO;
import org.gnrd.lam.service.IndexService;
import org.gnrd.lam.vo.CommonLoginVO;
import org.gnrd.lam.vo.MenuVO;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class LoginController {

    @Resource
    private IndexService indexService;

    @PostMapping(value = "auth/login")
    public ModelAndView auth(LoginRO loginRO, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        return indexService.login(loginRO.getUsername(), loginRO.getPassword(), request, response);
    }

    @PostMapping(value = "common/login")
    @ResponseBody
    public CommonResult<CommonLoginVO> commonLogin(@RequestBody @Validated LoginRO loginRO)
            throws Exception {
        CommonLoginVO login = indexService.login(loginRO.getUsername(), loginRO.getPassword());
        return new CommonResult<>(login);
    }

    @PostMapping(value = "auth/logout")
    @ResponseBody
    public CommonResult<String> logout(HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();
        session.invalidate();
        return new CommonResult<>("logout ok");
    }

    @GetMapping(value = "current-user")
    @ResponseBody
    public CommonResult<List<MenuVO>> me(@RequestHeader(AuthToken.COOKIE_NAME) String token)
            throws Exception {
        List<MenuVO> me = indexService.getMe(token);
        return new CommonResult<>(me);
    }

    @GetMapping(value = {"base-exception"})
    public CommonResult<Void> baseException(HttpServletRequest request) throws Exception {
        Object baseException = request.getAttribute("BaseException");
        throw (BaseException) baseException;
    }
}
