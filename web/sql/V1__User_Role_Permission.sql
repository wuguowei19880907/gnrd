create table c_user
(
    id         bigint
        constraint c_user_pk primary key
        constraint id_gt_zero check ( id > 0 ),
    c_name     varchar(255) not null unique ,
    c_phone    varchar(20) unique check ( length(c_phone)=11 ),
    c_password varchar(255),
    is_super smallint not null default 0
        constraint check_is_super check ( is_super = 0 or c_user.is_super = 1 ),
    c_state    smallint not null
        constraint state_in check ( c_state = 0 or c_user.c_state = 1 ),
    create_time timestamp
);
comment on column c_user.id is '主键';
comment on column c_user.c_name is '用户名';
comment on column c_user.c_phone is '手机号';
comment on column c_user.c_phone is '手机号';
comment on column c_user.c_password is '登录密码';
comment on column c_user.is_super is '是否未超级管理员；0-不是 1-是';
comment on column c_user.c_state is '用户状态；0-禁用 1-启用';
comment on column c_user.create_time is '用户创建时间';

create table c_role
(
    id bigint constraint c_role_pk primary key
        constraint id_gt_zero check ( id > 0 ),
    c_name varchar(255) not null unique,
    c_code varchar(255) not null unique,
    c_state    smallint not null default 1
        constraint state_in check ( c_state = 0 or c_role.c_state = 1 ),
    create_time timestamp
);

comment on column c_role.id is '主键';
comment on column c_role.c_name is '角色名';
comment on column c_role.c_code is '角色编码';
comment on column c_role.c_state is '状态；0-禁用 1-启用';
comment on column c_role.create_time is '创建时间';

create table c_user_role
(
    id bigint constraint c_user_role_pk primary key
        constraint id_gt_zero check ( id > 0 ),
    c_user_id bigint not null,
    c_role_id bigint not null,
    create_time timestamp
);

comment on column c_user_role.id is '主键';
comment on column c_user_role.c_user_id is '用户id';
comment on column c_user_role.c_role_id is '角色id';
comment on column c_role.create_time is '创建时间';

create table c_permission
(
    id bigint constraint c_permission_pk primary key
        constraint id_gt_zero check ( id > 0 ),
    c_name varchar(255) not null unique,
    c_code varchar(255) not null unique,
    c_state    smallint not null default 1
        constraint state_in check ( c_state = 0 or c_permission.c_state = 1 ),
    create_time timestamp
);

comment on column c_permission.id is '主键';
comment on column c_permission.c_name is '权限名';
comment on column c_permission.c_code is '权限编码';
comment on column c_permission.c_state is '状态；0-禁用 1-启用';
comment on column c_permission.create_time is '创建时间';

create table c_role_permission
(
    id bigint constraint c_role_permission_pk primary key
        constraint id_gt_zero check ( id > 0 ),
    c_role_id bigint not null,
    c_permission_id bigint not null,
    create_time timestamp
);

comment on column c_role_permission.id is '主键';
comment on column c_role_permission.c_role_id is '角色id';
comment on column c_role_permission.c_permission_id is '权限id';
comment on column c_role_permission.create_time is '创建时间';

