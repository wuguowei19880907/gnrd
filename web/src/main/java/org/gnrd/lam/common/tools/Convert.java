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

import java.util.ArrayList;
import java.util.List;

public class Convert {

	public static <T> List<T> toList(Object object, Class<T> clazz) {
		final List<T> result = new ArrayList<T>();
		if (object instanceof List<?>) {
			for (Object o : (List<?>) object) {
				result.add(clazz.cast(o));
			}
		}
		return result;
	}

	/**
	 * 将模糊查询参数中的 / _ %三个特殊字符转义，并在开头和结尾添加 % 后返回
	 *
	 * @author wuguowei
	 * @param param
	 *            原始查询条件
	 * @return 转换后的查询条件
	 */
	public static String getFuzzyQueryParam(final String param) {
		if (param == null) {
			return null;
		}
		return "%" + param.replace("/", "\\/").replace("_", "\\_").replace("%", "\\%") + "%";
	}
}
