create database if not exists `user` default charset utf8mb4 collate utf8mb4_general_ci;

use `user`;

drop table if exists `tbl_xxx_user` ;
create table `tbl_xxx_user`
(
	`id` int not null auto_increment,
	`name` varchar(50) not null comment '姓名',
	`email` varchar(100) comment '邮箱',
	
	primary key (`id`)
) engine=innodb auto_increment=1 default charset=utf8mb4 comment='用户表';

INSERT INTO `tbl_xxx_user` VALUES (1, 'zhangsan', 'zhangsan@hotmail.com');
INSERT INTO `tbl_xxx_user` VALUES (2, 'lisi', 'lisi@sina.com');



