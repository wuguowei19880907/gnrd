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

import org.gnrd.uid.impl.CachedUidGenerator;
import org.gnrd.uid.worker.DisposableWorkerIdAssigner;
import org.gnrd.uid.worker.WorkerIdAssigner;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import javax.annotation.Resource;

@AutoConfiguration
@EnableConfigurationProperties({UidProperties.class})
@ComponentScan({"org.gnrd.uid"})
public class UtilConfiguration {

    @Resource
    private UidProperties uidProperties;

    @Bean
    public DisposableWorkerIdAssigner disposableWorkerIdAssigner() {
        return new DisposableWorkerIdAssigner();
    }

    @Bean
    public CachedUidGenerator cachedUidGenerator(WorkerIdAssigner disposableWorkerIdAssigner) {
        CachedUidGenerator cachedUidGenerator = new CachedUidGenerator();
        cachedUidGenerator.setWorkerIdAssigner(disposableWorkerIdAssigner);

        // 关于UID比特分配的建议： 对于并发数要求不高、期望长期使用的应用, 可增加timeBits位数, 减少seqBits位数.
        // 例如节点采取用完即弃的WorkerIdAssigner策略, 重启频率为12次/天,
        // 那么配置成： {"workerBits":23,"timeBits":31,"seqBits":9}时, 可支持28个节点以整体并发量14400
        // UID/s的速度持续运行68年.

        // 对于节点重启频率频繁、期望长期使用的应用, 可增加workerBits和timeBits位数, 减少seqBits位数.
        // 例如节点采取用完即弃的WorkerIdAssigner策略, 重启频率为24*12次/天,
        // 那么配置成： {"workerBits":27,"timeBits":30,"seqBits":6}时, 可支持37个节点以整体并发量2400
        // UID/s的速度持续运行34年.

        // 以下为可选配置, 如未指定将采用默认值
        cachedUidGenerator.setTimeBits(uidProperties.getTimeBits());
        // 机器id，最多可支持2^22约420w次机器启动。内置实现为在启动时由数据库分配，默认分配策略为用后即弃，后续可提供复用策略。
        cachedUidGenerator.setWorkerBits(uidProperties.getWorkerBits());
        // 每秒下的并发序列，9 bits可支持每台服务器每秒512个并发。
        cachedUidGenerator.setSeqBits(uidProperties.getSeqBits());

        cachedUidGenerator.setEpochStr(uidProperties.getEpochStr());

        // RingBuffer size扩容参数, 可提高UID生成的吞吐量
        // 默认:3， 原bufferSize=8192, 扩容后bufferSize= 8192 << 3 = 65536
        cachedUidGenerator.setBoostPower(uidProperties.getBoostPower());
        // 指定何时向RingBuffer中填充UID, 取值为百分比(0, 100), 默认为50
        // 举例: bufferSize=1024, paddingFactor=50 -> threshold=1024 * 50 / 100 = 512.
        // 当环上可用UID数量 < 512时, 将自动对RingBuffer进行填充补全
        // <property name="paddingFactor" value="50"></property>

        // 另外一种RingBuffer填充时机, 在Schedule线程中, 周期性检查填充
        // 默认:不配置此项, 即不实用Schedule线程. 如需使用, 请指定Schedule线程时间间隔, 单位:秒
        cachedUidGenerator.setScheduleInterval(uidProperties.getScheduleInterval());

        // 拒绝策略: 当环已满, 无法继续填充时
        // 默认无需指定, 将丢弃Put操作, 仅日志记录. 如有特殊需求, 请实现RejectedPutBufferHandler接口(支持Lambda表达式)
        // <property name="rejectedPutBufferHandler"
        // ref="XxxxYourPutRejectPolicy"></property>
        // cachedUidGenerator.setRejectedPutBufferHandler();
        // 拒绝策略: 当环已空, 无法继续获取时 -->
        // 默认无需指定, 将记录日志, 并抛出UidGenerateException异常. 如有特殊需求,
        // 请实现RejectedTakeBufferHandler接口(支持Lambda表达式) -->
        // <property name="rejectedTakeBufferHandler"
        // ref="XxxxYourTakeRejectPolicy"></property>

        return cachedUidGenerator;
    }
}
