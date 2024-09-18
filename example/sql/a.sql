create table "student"
(
    "id"   bigint
        constraint student_pk primary key
        constraint id_gt_zero check ( id > 0 ),
    "stu_name" varchar(255) not null unique,
    "stu_addr" varchar(255) default null
)