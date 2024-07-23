CREATE TABLE "tb_generator" (
                                "id" bigint NOT NULL primary key ,
                                "gen_name" varchar(255) NOT NULL,
                                "gen_value" bigint NOT NULL
);
INSERT INTO "tb_generator" ("id", "gen_name", "gen_value") VALUES ('1', 'WorkNodeId', '1');


CREATE TABLE "worker_node" (
                               "id" bigint NOT NULL primary key,
                               "host_name" varchar(64) NOT NULL,
                               "port" varchar(64) NOT NULL,
                               "type" int NOT NULL,
                               "launch_date" date NOT NULL,
                               "modified" timestamp NOT NULL,
                               "created" timestamp NOT NULL
);
comment on column "worker_node"."id" is 'id';
comment on column "worker_node"."host_name" is 'host name';
comment on column "worker_node"."port" is 'port';
comment on column "worker_node"."type" is 'node type: ACTUAL or CONTAINER';
comment on column "worker_node"."launch_date" is 'launch date';
comment on column "worker_node"."modified" is 'modified time';
comment on column "worker_node"."created" is 'created time';