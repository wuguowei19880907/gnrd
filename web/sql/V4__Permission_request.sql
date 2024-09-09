create table df_request_mapping
(
    id          bigint
        constraint df_request_mapping_pk primary key
        constraint id_gt_zero check ( id > 0 ),
    df_name     varchar(255) unique,
    df_path     varchar(255) not null,
    df_method   varchar(255),
    df_params   varchar(255),
    df_headers  varchar(255),
    df_consumes varchar(255),
    df_produces varchar(255),
    is_lost     smallint     not null
        constraint is_lost_in check ( is_lost = 0 or is_lost = 1 ),
    create_time timestamp,
    update_time timestamp
);
comment on column df_request_mapping.id is '主键';
comment on column df_request_mapping.df_name is 'RequestMapping.name';
comment on column df_request_mapping.df_path is 'RequestMapping.path';
comment on column df_request_mapping.df_method is 'RequestMapping.method';
comment on column df_request_mapping.df_params is 'RequestMapping.params';
comment on column df_request_mapping.df_headers is 'RequestMapping.headers';
comment on column df_request_mapping.df_consumes is 'RequestMapping.consumes';
comment on column df_request_mapping.df_produces is 'RequestMapping.produces';
comment on column df_request_mapping.is_lost is 'url是否还存在，有可能被移除或修改';
comment on column df_request_mapping.create_time is '创建时间';
comment on column df_request_mapping.update_time is '修改时间';

create table df_permission_request
(
    id bigint
        constraint df_permission_request_pk primary key
        constraint id_gt_zero check ( id > 0 ),
    df_permission_id bigint not null references c_permission(id),
    df_request_id bigint not null  references df_request_mapping(id),
    create_time timestamp
);
comment on column df_permission_request.id is '主键';
comment on column df_permission_request.df_permission_id is '权限表id';
comment on column df_permission_request.df_request_id is 'RequestMapping表id';
comment on column df_permission_request.create_time is '创建时间';