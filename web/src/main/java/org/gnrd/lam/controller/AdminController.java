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

import org.gnrd.lam.common.result.ParamPager;
import org.gnrd.lam.service.IndexService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("admin")
public class AdminController {

    @Resource
    private IndexService indexService;

    /**
     * 获取用户信息列表
     * 
     * @author wuguowei
     * @param username 用户名，支持模糊查询|user0
     */
    @GetMapping(value = "users")
    public ModelAndView users(String username, ParamPager pager) throws Exception {
        ModelAndView modelAndView = new ModelAndView("admin/adminUsers");
        modelAndView.addObject("currentMenu", "admin_user_M");
        return modelAndView;
    }

    @GetMapping(value = "roles")
    public ModelAndView roles(HttpServletRequest request) throws Exception {
        ModelAndView modelAndView = new ModelAndView();
        return modelAndView;
    }

    @GetMapping(value = "permissions")
    public ModelAndView permissions(HttpServletRequest request) throws Exception {
        ModelAndView modelAndView = new ModelAndView();
        return modelAndView;
    }
}
