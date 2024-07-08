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

package org.gnrd.uid.worker.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import java.io.Serializable;
import java.util.Date;

/**
 * Entity for M_WORKER_NODE
 *
 * @author WUGUOWEI 2024年06月28日
 */
@Entity
@Table(name = "worker_node")
public class WorkerNodeEntity implements Serializable {

	@Id
	@Column(updatable = false)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "WorkNodeId")
	@TableGenerator(name = "WorkNodeId", table = "tb_generator", pkColumnName = "gen_name", schema = "public", valueColumnName = "gen_value", pkColumnValue = "WorkNodeId", allocationSize = 1)
	private Long id;

	@Column(name = "host_name")
	private String hostName;

	@Column(name = "port")
	private String port;

	@Column(name = "type")
	private Integer type;

	@Column(name = "launch_date")
	private Date launchDate = new Date();

	@Column(name = "modified")
	private Date modified = new Date();

	@Column(name = "created")
	private Date created = new Date();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Date getLaunchDate() {
		return launchDate;
	}

	public void setLaunchDate(Date launchDate) {
		this.launchDate = launchDate;
	}

	public Date getModified() {
		return modified;
	}

	public void setModified(Date modified) {
		this.modified = modified;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	@Override
	public String toString() {
		return "WorkerNodeEntity{" + "id=" + id + ", hostName='" + hostName + '\'' + ", port='" + port + '\''
				+ ", type=" + type + ", launchDate=" + launchDate + ", modified=" + modified + ", created=" + created
				+ '}';
	}
}
