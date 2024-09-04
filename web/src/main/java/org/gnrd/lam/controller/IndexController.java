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

import org.gnrd.lam.aop.auth.Authorize;
import org.gnrd.lam.common.result.CommonResult;
import org.gnrd.lam.dto.LoginUserDTO;
import org.gnrd.lam.service.IndexService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("api")
public class IndexController {

    @Resource
    private IndexService indexService;

    @GetMapping(name = "abcde", value = "me", params = {"abc", "def"}, headers = {"auth=text/*"},
            consumes = {"text/plain", "application/*"}, produces = {"text/plain", "application/*"})
    @ResponseBody
    @Authorize("@authUtil.hasPermissions('get_user_me')")
    public CommonResult<String> getInfo(HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();
        LoginUserDTO loginUser = (LoginUserDTO) session.getAttribute("login-user");
        return new CommonResult<>(loginUser.getName());
    }

    @RequestMapping(value = {"index", ""})
    public String index() {
        return "index";
    }

    @GetMapping(value = "super/dashboard")
    @Authorize("@authUtil.hasLogin()")
    public String superDashboard() {
        return "dashboard";
    }
}
