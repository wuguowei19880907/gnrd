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

package org.gnrd.lam.common.exception;

import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * controller 增强器
 *
 * @author Danner
 * @since 2018/03/16
 */
@RestControllerAdvice(basePackages = {"org.gnrd.lam.controller"})
@Slf4j
public class GlobalRestControllerAdvice {

	/**
	 * 应用到所有@RequestMapping注解方法，在其执行之前初始化数据绑定器
	 */
	@InitBinder
	public void initBinder(WebDataBinder binder) {

	}

	/**
	 * 把值绑定到Model中，使全局@RequestMapping可以获取到该值
	 */
	@ModelAttribute
	public void addAttributes(Model model) {

	}

	/**
	 * 全局异常捕捉处理
	 */
	@ExceptionHandler(value = Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public Map<String, Object> errorHandler(Exception ex, HttpServletRequest request, HttpServletResponse response) {
		log.error("全局异常：", ex);
		Map<String, Object> map = new HashMap<>();
		map.put("code", 100000);
		map.put("message", ex.getMessage());
		return map;
	}

	/**
	 * 拦截捕捉自定义异常 BaseException.class
	 */
	@ExceptionHandler(value = BaseException.class)
	public Map<String, Object> baseErrorHandler(BaseException ex, HttpServletRequest request,
												HttpServletResponse response) {
		// baseException只记录信息，不记录堆栈，堆栈信息太多了
		log.error("错误编码：{}。错误描述：{}", ex.getCode(), ex.getMessage());
		Map<String, Object> map = new HashMap<>();
		map.put("code", ex.getCode());
		map.put("message", ex.getMessage());
		response.setStatus(ex.getStatus().value());
		return map;
	}

	/**
	 * 拦截捕捉验证异常[实体类检测]
	 */
	@ExceptionHandler(value = MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public Map<String, Object> verificationErrorHandler(MethodArgumentNotValidException ex, HttpServletRequest request,
														HttpServletResponse response) {
		log.error("参数检验异常：", Throwables.getRootCause(ex));
		Map<String, Object> map = new HashMap<>(2);
		map.put("code", ECode.E_999998.getCode());
		map.put("message", ECode.E_999998.getMessage());
		List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
		Map<Object, Object> errors = new HashMap<>(fieldErrors.size());
		for (FieldError fieldError : fieldErrors) {
			errors.put(fieldError.getField(), fieldError.getDefaultMessage());
		}
		map.put("errors", errors);

		return map;
	}

	/**
	 * 拦截捕捉验证异常[方法参数检测]
	 */
	@ExceptionHandler(value = ValidationException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public Map<String, Object> handle(ValidationException ex, HttpServletRequest request,
									  HttpServletResponse response) {
		log.error("检验异常：", Throwables.getRootCause(ex));
		Map<String, Object> map = new HashMap<>(2);
		map.put("code", ECode.E_999998.getCode());
		map.put("message", ECode.E_999998.getMessage());
		Map<Object, Object> errors = new HashMap<>();
		if (ex instanceof ConstraintViolationException) {
			ConstraintViolationException exs = (ConstraintViolationException) ex;
			Set<ConstraintViolation<?>> violations = exs.getConstraintViolations();
			for (ConstraintViolation<?> item : violations) {
				String[] strs = item.getMessage().split(":");
				if (strs.length > 1) {
					errors.put(strs[0], strs[1]);
				} else {
					errors.put(item.getPropertyPath(), item.getMessage());
				}
			}
		}
		map.put("errors", errors);
		return map;
	}

	/**
	 * 拦截捕捉验证异常[实体类检测]
	 */
	@ExceptionHandler(value = BindException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public Map<String, Object> handle(BindException ex, HttpServletRequest request, HttpServletResponse response) {
		log.error("BindException：", Throwables.getRootCause(ex));
		Map<String, Object> map = new HashMap<>(2);
		map.put("code", ECode.E_999998.getCode());
		map.put("message", ECode.E_999998.getMessage());
		List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
		Map<Object, Object> errors = new HashMap<>(fieldErrors.size());
		for (FieldError fieldError : fieldErrors) {
			errors.put(fieldError.getField(), fieldError.getDefaultMessage());
		}
		map.put("errors", errors);
		return map;
	}

}
