insert into c_permission(id, c_name, c_code, c_state, create_time)
VALUES (1,'超级管理员','super',1,'2024-07-01 10:00:00'),
       (2,'查看个人信息','get_user_me',1,'2024-07-01 10:00:00');

insert into c_role(id, c_name, c_code, c_state, create_time)
VALUES (1,'超管角色','super',1,'2024-07-01 10:00:00'),
       (2,'公共角色','public',1,'2024-07-01 10:00:00');

insert into c_role_permission(id, c_role_id, c_permission_id, create_time)
VALUES (1,1,1,'2024-07-01 10:00:00'),
       (2,2,2,'2024-07-01 10:00:00');

insert into c_user(id, c_name, c_phone, c_password, c_state, create_time)
VALUES (1,'sa','18522221111','123456',1,'2024-07-01 10:00:00'),
       (2,'test','18522221112','123456',1,'2024-07-01 10:00:00');

insert into c_user_role(id, c_user_id, c_role_id, create_time)
values (1,1,1,'2024-07-01 10:00:00'),
       (2,2,2,'2024-07-01 10:00:00');