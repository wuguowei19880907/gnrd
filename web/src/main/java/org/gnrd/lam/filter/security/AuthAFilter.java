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
import org.gnrd.lam.common.exception.BaseException;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.gnrd.lam.common.exception.ECode.E_100001;

/**
 * 判断请求是否包含X-Auth-Token，如果不包含则直接返回错误
 *
 * @author WUGUOWEI 2024年07月17日
 */
@WebFilter(urlPatterns = {"/api/*", "/auth/logout"}, servletNames = {"authAFilter"})
@Slf4j
public class AuthAFilter implements Filter {

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		String authToken = request.getHeader("X-Auth-Token");
		if (!StringUtils.hasText(authToken) || authToken.equals("null")) {
			log.error("X-Auth-Token is " + authToken);
			request.setAttribute("BaseException", new BaseException(HttpStatus.UNAUTHORIZED, E_100001));
			request.getRequestDispatcher("/base-exception").forward(request, servletResponse);
			return;
		}
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		response.setHeader("X-Auth-Token", authToken);
		filterChain.doFilter(servletRequest, servletResponse);
	}
}
