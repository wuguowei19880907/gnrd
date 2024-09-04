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

package org.gnrd.uid.worker.dao;

import org.gnrd.uid.worker.entity.WorkerNodeEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

/**
 * DAO for M_WORKER_NODE
 *
 * @author yutianbao
 */
@Component
public class WorkerNodeDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(WorkerNodeDAO.class);

    private final String insertWorkNodeSql =
            "insert into worker_node (id, host_name, port, type, launch_date, modified, created) values (?,?,?,?,?,?,?)";
    private final String updateGeneratorSql = "update tb_generator set gen_value=? where id=?";
    private final int generatorId = 1;

    @Resource
    private DataSource dataSource;

    public void save(WorkerNodeEntity workerNode) {
        try (Connection connection = dataSource.getConnection()) {
            // 关闭自动提交
            connection.setAutoCommit(false);
            try (PreparedStatement ps1 = connection
                    .prepareStatement("select gen_value from tb_generator where id = ?")) {
                ps1.setLong(1, generatorId);
                try (ResultSet resultSet = ps1.executeQuery()) {
                    while (resultSet.next()) {
                        long aLong = resultSet.getLong(1);
                        // 插入work node表数据
                        try (PreparedStatement ps2 =
                                connection.prepareStatement(insertWorkNodeSql)) {
                            ps2.setLong(1, aLong);
                            ps2.setString(2, workerNode.getHostName());
                            ps2.setString(3, workerNode.getPort());
                            ps2.setInt(4, workerNode.getType());
                            ps2.setDate(5, new Date(workerNode.getLaunchDate().getTime()));
                            ps2.setTimestamp(6, new Timestamp(workerNode.getModified().getTime()));
                            ps2.setTimestamp(7, new Timestamp(workerNode.getCreated().getTime()));
                            int i = ps2.executeUpdate();
                            if (i != 1) {
                                throw new RuntimeException("插入worker_node数据失败");
                            }
                        }
                        workerNode.setId(aLong);
                        // 修改tb_generator数据
                        aLong++;
                        try (PreparedStatement ps3 =
                                connection.prepareStatement(updateGeneratorSql)) {
                            ps3.setLong(1, aLong);
                            ps3.setLong(2, generatorId);
                            int i = ps3.executeUpdate();
                            if (i != 1) {
                                throw new RuntimeException("更新tb_generator数据失败");
                            }
                        }
                    }
                }
                connection.commit();
            } catch (Exception e) {
                LOGGER.error("", e);
                connection.rollback();
            }
        } catch (Exception e) {
            LOGGER.error("", e);
        }
    }
}
