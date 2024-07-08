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

package org.gnrd.uid.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.Assert;

@ConfigurationProperties(prefix = "uid")
public class UidProperties {

	/**
	 * 时间位
	 * ----------------------------------------------------------------------------------
	 * ----------sign:1bit | deltaSecoonds:32bit | workerNode:22bit |
	 * sequence:9bit------ 关于UID比特分配的建议： 对于并发数要求不高、期望长期使用的应用, 可增加timeBits位数,
	 * 减少seqBits位数. 例如节点采取用完即弃的WorkerIdAssigner策略, 重启频率为12次/天, 那么配置成：
	 * {"timeBits":31,"workerBits":23,"seqBits":9}时, 可支持28个节点以整体并发量14400
	 * UID/s的速度持续运行68年. 对于节点重启频率频繁、期望长期使用的应用, 可增加workerBits和timeBits位数, 减少seqBits位数.
	 * 例如节点采取用完即弃的WorkerIdAssigner策略, 重启频率为24*12次/天, 那么配置成：
	 * {"timeBits":30,"workerBits":27,"seqBits":6}时, 可支持37个节点以整体并发量2400
	 * UID/s的速度持续运行34年.
	 * ----------------------------------------------------------------------------------
	 */
	private Integer timeBits = 31;

	/**
	 * 机器id，最多可支持2^22约420w次机器启动。内置实现为在启动时由数据库分配，默认分配策略为用后即弃，后续可提供复用策略。
	 */
	private Integer workerBits = 23;

	/**
	 * 每秒下的并发序列，9 bits可支持每台服务器每秒512个并发。
	 */
	private Integer seqBits = 9;

	/**
	 * 服务的时期(最好设置服务器部署的时间,避免造成时间浪费)
	 */
	private String epochStr;

	/**
	 * RingBuffer size扩容参数, 可提高UID生成的吞吐量 默认:3， 原bufferSize=8192, 扩容后bufferSize= 8192
	 * << 3 = 65536
	 */
	private Integer boostPower = 3;

	/**
	 * 默认:不配置此项, 即不实用Schedule线程. 如需使用, 请指定Schedule线程时间间隔, 单位:秒
	 */
	private Integer scheduleInterval = 120;

	public Integer getTimeBits() {
		return timeBits;
	}

	public void setTimeBits(Integer timeBits) {
		this.timeBits = timeBits;
	}

	public Integer getWorkerBits() {
		return workerBits;
	}

	public void setWorkerBits(Integer workerBits) {
		this.workerBits = workerBits;
	}

	public Integer getSeqBits() {
		return seqBits;
	}

	public void setSeqBits(Integer seqBits) {
		this.seqBits = seqBits;
	}

	public String getEpochStr() {
		Assert.hasText(epochStr, "uid.epochStr 不可位空");
		return epochStr;
	}

	public void setEpochStr(String epochStr) {
		this.epochStr = epochStr;
	}

	public Integer getBoostPower() {
		return boostPower;
	}

	public void setBoostPower(Integer boostPower) {
		this.boostPower = boostPower;
	}

	public Integer getScheduleInterval() {
		return scheduleInterval;
	}

	public void setScheduleInterval(Integer scheduleInterval) {
		this.scheduleInterval = scheduleInterval;
	}
}
