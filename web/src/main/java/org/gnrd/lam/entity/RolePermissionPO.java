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
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Entity
@Table(name = "c_role_permission")
@Setter
@Getter
public class RolePermissionPO extends BasePO {

	/**
	 * 角色id
	 */
	@Column(name = "c_role_id")
	private Long roleId;

	/**
	 * 权限id
	 */
	@Column(name = "c_permission_id")
	private Long permissionId;

	/**
	 * 创建时间
	 */
	@Column(name = "create_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt;

	@OneToOne
	@JoinColumn(name = "c_permission_id", insertable = false, updatable = false)
	private PermissionPO permissionPO;

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof RolePermissionPO))
			return false;

		RolePermissionPO entity = (RolePermissionPO) o;

		return id.equals(entity.getId());
	}

	@Override
	public int hashCode() {
		return id != null ? id.hashCode() : 0;
	}
}
