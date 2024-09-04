create table df_menu
(
    id          bigint
        constraint df_menu_pk primary key
        constraint id_gt_zero check ( id > 0 ),
    df_name     varchar(255) not null unique,
    df_code     varchar(255) not null unique,
    df_path     varchar(255) not null,
    df_sort   integer constraint sort_gt_zero check ( df_sort > 0 ),
    df_status     smallint     not null
        constraint status_in check ( df_status = 0 or df_status = 1 ),
    create_time timestamp
);
comment on column df_menu.id is '主键';
comment on column df_menu.df_name is '菜单名称';
comment on column df_menu.df_code is '菜单编码';
comment on column df_menu.df_path is '菜单的url';
comment on column df_menu.df_sort is '排序，值越大，菜单越靠上';
comment on column df_menu.df_status is '菜单状态；0-禁用 1-启用';
comment on column df_menu.create_time is '创建时间';

create table df_role_menu
(
    id bigint
        constraint df_role_menu_pk primary key
        constraint id_gt_zero check ( id > 0 ),
    df_role_id bigint not null references c_role(id),
    df_menu_id bigint not null  references df_menu(id),
    create_time timestamp
);
comment on column df_role_menu.id is '主键';
comment on column df_role_menu.df_role_id is '角色表id';
comment on column df_role_menu.df_menu_id is '菜单表id';
comment on column df_role_menu.create_time is '创建时间';


insert into df_menu(id, df_name, df_code, df_path, df_sort, df_status, create_time)
VALUES (1, '后台角色管理', 'admin_role_M', '#', 1, 1, now()),
       (2, '后台用户管理', 'admin_user_M', '#', 2, 1, now()),
       (3, '后台权限管理', 'admin_permission_M', '#', 3, 1, now());

insert into df_role_menu(id, df_role_id, df_menu_id, create_time)
VALUES (1,1,1,now()),
       (2,1,2,now()),
       (3,1,3,now());