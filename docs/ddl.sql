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


drop table if exists `tbl_xxx_ticket` ;
create table `tbl_xxx_ticket`
(
	`id` int not null auto_increment,
	
	`user_id` int not null default 0 comment '所属用户',
	
	`code` varchar(50) not null comment '票据代码',
	
	primary key (`id`)
) engine=innodb auto_increment=1 default charset=utf8mb4 comment='票据表';

INSERT INTO `tbl_xxx_ticket` VALUES (1, '1', 'T-ak19271j');
INSERT INTO `tbl_xxx_ticket` VALUES (2, '1', 'T-28193781');
