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

package org.gnrd.lam.common.result;

import javax.validation.constraints.Min;
import java.io.Serializable;

/**
 * 分页查询参数
 *
 * @author WUGUOWEI 2024年08月22日
 */
public class ParamPager implements Serializable {

	/**
	 * 分页参数，第几页，从1开始，默认为1
	 * 
	 * @mock 1
	 */
	@Min(message = "分页参数number最小为1", value = 1)
	private Integer number = 1;

	/**
	 * 分页参数，每一页有几条记录，最少为1，默认为10
	 * 
	 * @mock 10
	 */
	@Min(message = "分页参数size最小为1", value = 1)
	private Integer size = 10;

	public ParamPager() {
	}

	public ParamPager(Integer number, Integer size) {
		this.number = number;
		this.size = size;
	}

	public ParamPager(Integer size) {
		this.size = size;
	}

	public Integer getNumber() {
		return number - 1;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

}
