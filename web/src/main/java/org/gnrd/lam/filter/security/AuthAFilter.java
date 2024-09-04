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

package org.gnrd.lam.filter.security;

import lombok.extern.slf4j.Slf4j;
import org.gnrd.lam.common.constants.AuthToken;
import org.gnrd.lam.common.tools.SessionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 判断请求是否包含X-Auth-Token，如果不包含则直接返回错误
 *
 * @author WUGUOWEI 2024年07月17日
 */
@WebFilter(urlPatterns = {"/admin/*", "/auth/logout"}, servletNames = {"authAFilter"})
@Slf4j
public class AuthAFilter implements Filter {

    @Resource
    private SessionUtils sessionUtils;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
            FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String authToken = getTokenCookie(request);
        if (!StringUtils.hasText(authToken)) {
            log.error("X-Auth-Token is " + authToken);
            HttpServletResponse response = (HttpServletResponse) servletResponse;
            response.sendRedirect("/");
            return;
        }
        Object info = sessionUtils.getInfo(authToken);
        servletRequest.setAttribute(AuthToken.ATTRIBUTE, info);
        filterChain.doFilter(servletRequest, servletResponse);
    }

    private String getTokenCookie(HttpServletRequest request) {
        if (request.getCookies() == null) {
            return null;
        }
        for (Cookie cookie : request.getCookies()) {
            if (AuthToken.COOKIE_NAME.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
