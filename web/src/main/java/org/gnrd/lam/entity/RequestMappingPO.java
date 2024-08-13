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

package org.gnrd.lam.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Entity
@Table(name = "df_request_mapping")
@Setter
@Getter
public class RequestMappingPO extends BasePO {

	/**
	 * RequestMapping.name
	 */
	@Column(name = "df_name")
	private String name;

	/**
	 * RequestMapping.path
	 */
	@Column(name = "df_path")
	private String path;

	/**
	 * RequestMapping.method
	 */
	@Column(name = "df_method")
	private String method;

	/**
	 * RequestMapping.params
	 */
	@Column(name = "df_params")
	private String params;

	/**
	 * RequestMapping.headers
	 */
	@Column(name = "df_headers")
	private String headers;

	/**
	 * RequestMapping.consumes
	 */
	@Column(name = "df_consumes")
	private String consumes;

	/**
	 * RequestMapping.produces
	 */
	@Column(name = "df_produces")
	private String produces;

	/**
	 * url是否还存在，有可能被移除或修改
	 */
	@Column(name = "is_lost")
	private Integer isLost;

	/**
	 * 创建时间
	 */
	@Column(name = "create_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt;

	/**
	 * 更新时间
	 */
	@Column(name = "update_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updatedAt;

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof RequestMappingPO))
			return false;

		RequestMappingPO entity = (RequestMappingPO) o;

		return id.equals(entity.getId());
	}

	@Override
	public int hashCode() {
		return id != null ? id.hashCode() : 0;
	}
}
