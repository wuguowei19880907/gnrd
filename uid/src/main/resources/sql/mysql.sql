CREATE TABLE `tb_generator` (
                                `id` bigint NOT NULL,
                                `gen_name` varchar(255) NOT NULL,
                                `gen_value` bigint NOT NULL,
                                PRIMARY KEY (`id`)
);
INSERT INTO `tb_generator` (`id`, `gen_name`, `gen_value`) VALUES ('1', 'WorkNodeId', '1');


CREATE TABLE `worker_node` (
                               `id` bigint NOT NULL COMMENT 'id',
                               `host_name` varchar(64) NOT NULL COMMENT 'host name',
                               `port` varchar(64) NOT NULL COMMENT 'port',
                               `type` int NOT NULL COMMENT 'node type: ACTUAL or CONTAINER',
                               `launch_date` date NOT NULL COMMENT 'launch date',
                               `modified` timestamp NOT NULL COMMENT 'modified time',
                               `created` timestamp NOT NULL COMMENT 'created time',
                               PRIMARY KEY (`id`)
) COMMENT='DB WorkerID Assigner for UID Generator';