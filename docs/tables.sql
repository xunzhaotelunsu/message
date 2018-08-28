

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for email_send_server
-- ----------------------------
DROP TABLE IF EXISTS `email_send_server`;
CREATE TABLE `email_send_server` (
  `server_code` varchar(16) NOT NULL COMMENT '邮件服务代码',
  `host` varchar(128) NOT NULL COMMENT 'smtp服务器地址',
  `port` int(11) NOT NULL COMMENT 'smtp服务器端口',
  `username` varchar(128) NOT NULL COMMENT '用户名',
  `password` varchar(32) NOT NULL COMMENT '密码',
  `personal` varchar(32) NOT NULL COMMENT '发送服务器别名',
  `active` varchar(8) NOT NULL COMMENT '是否启用',
  `priority` int(11) NOT NULL COMMENT '优先级',
  `create_time` varchar(24) DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`server_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='邮件发送服务';

-- ----------------------------
-- Table structure for groups
-- ----------------------------
DROP TABLE IF EXISTS `groups`;
CREATE TABLE `groups` (
  `group_id` varchar(64) NOT NULL COMMENT '群组id',
  `group_name` varchar(64) DEFAULT NULL COMMENT '群组名',
  `create_time` varchar(24) DEFAULT NULL,
  PRIMARY KEY (`group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户群组';

-- ----------------------------
-- Table structure for message_biz_type
-- ----------------------------
DROP TABLE IF EXISTS `message_biz_type`;
CREATE TABLE `message_biz_type` (
  `message_biz_type` varchar(32) NOT NULL COMMENT '消息业务类型code',
  `source_code` varchar(8) DEFAULT NULL COMMENT '业务系统code',
  `type_name` varchar(128) NOT NULL COMMENT '消息业务类型名称',
  `rate_limit` int(11) DEFAULT '-1' COMMENT '流控（单个地址每分钟限制发送数）',
  `limit_unit` varchar(8) DEFAULT NULL,
  `create_time` varchar(24) DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`message_biz_type`),
  KEY `FK_Reference_1` (`source_code`),
  CONSTRAINT `FK_Reference_1` FOREIGN KEY (`source_code`) REFERENCES `message_source` (`source_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='消息业务类型';

-- ----------------------------
-- Table structure for message_info
-- ----------------------------
DROP TABLE IF EXISTS `message_info`;
CREATE TABLE `message_info` (
  `message_id` varchar(32) NOT NULL,
  `source_code` varchar(32) NOT NULL COMMENT '业务系统code',
  `message_biz_type` varchar(32) NOT NULL COMMENT '消息业务类型code',
  `message_biz_id` varchar(64) DEFAULT NULL COMMENT '业务id',
  `message_sender` varchar(32) DEFAULT NULL COMMENT '消息发送者',
  `message_receiver` varchar(32) NOT NULL COMMENT '消息接受者',
  `message_receiver_type` varchar(8) NOT NULL COMMENT '消息接受者类型（single 或 group）',
  `message_receiver_address` varchar(128) DEFAULT NULL COMMENT '接受者地址（可空）',
  `title` varchar(128) DEFAULT NULL COMMENT '标题',
  `content` mediumtext COMMENT '内容（已格式化或待格式化）',
  `pre_status` varchar(8) DEFAULT NULL COMMENT '消息预处理状态',
  `remark` varchar(128) DEFAULT NULL COMMENT '备注信息',
  `privilege` varchar(8) DEFAULT NULL COMMENT '消息紧急度',
  `create_time` varchar(24) DEFAULT NULL COMMENT '创建时间',
  `attachments` mediumtext,
  `extra_info` mediumtext COMMENT '其余信息',
  PRIMARY KEY (`message_id`),
  KEY `FK_Reference_5` (`source_code`),
  KEY `FK_Reference_6` (`message_biz_type`),
  CONSTRAINT `FK_Reference_5` FOREIGN KEY (`source_code`) REFERENCES `message_source` (`source_code`),
  CONSTRAINT `FK_Reference_6` FOREIGN KEY (`message_biz_type`) REFERENCES `message_biz_type` (`message_biz_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='消息';

-- ----------------------------
-- Table structure for message_send
-- ----------------------------
DROP TABLE IF EXISTS `message_send`;
CREATE TABLE `message_send` (
  `message_id` varchar(32) NOT NULL COMMENT '消息主键',
  `receiver_id` varchar(64) NOT NULL COMMENT '接受者id',
  `message_send_type` varchar(10) NOT NULL COMMENT '消息发送类型',
  `receiver_address` varchar(128) NOT NULL COMMENT '接受者地址',
  `send_status` varchar(8) DEFAULT NULL COMMENT '发送状态',
  `send_time` varchar(24) DEFAULT NULL COMMENT '发送时间',
  `read_status` varchar(8) DEFAULT NULL COMMENT '读取状态',
  `read_time` varchar(24) DEFAULT NULL COMMENT '读取时间',
  `remark` varchar(128) DEFAULT NULL COMMENT '备注信息',
  PRIMARY KEY (`message_id`,`receiver_id`,`message_send_type`),
  CONSTRAINT `FK_Reference_7` FOREIGN KEY (`message_id`) REFERENCES `message_info` (`message_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='消息发送详情';

-- ----------------------------
-- Table structure for message_send_type
-- ----------------------------
DROP TABLE IF EXISTS `message_send_type`;
CREATE TABLE `message_send_type` (
  `message_send_type` varchar(10) NOT NULL COMMENT '发送类型code',
  `message_biz_type` varchar(32) NOT NULL COMMENT '消息业务类型code',
  PRIMARY KEY (`message_send_type`,`message_biz_type`),
  KEY `FK_Reference_4` (`message_biz_type`),
  CONSTRAINT `FK_Reference_3` FOREIGN KEY (`message_send_type`) REFERENCES `send_type` (`message_send_type`),
  CONSTRAINT `FK_Reference_4` FOREIGN KEY (`message_biz_type`) REFERENCES `message_biz_type` (`message_biz_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='业务消息发送类型';

-- ----------------------------
-- Table structure for message_source
-- ----------------------------
DROP TABLE IF EXISTS `message_source`;
CREATE TABLE `message_source` (
  `source_code` varchar(32) NOT NULL COMMENT '业务系统code',
  `source_name` varchar(128) NOT NULL COMMENT '业务系统名称',
  `password` varchar(256) NOT NULL COMMENT '密码',
  `create_time` varchar(24) DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`source_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='业务系统';

-- ----------------------------
-- Table structure for message_template
-- ----------------------------
DROP TABLE IF EXISTS `message_template`;
CREATE TABLE `message_template` (
  `message_biz_type` varchar(32) NOT NULL COMMENT '消息业务类型code',
  `message_send_type` varchar(10) NOT NULL COMMENT '发送类型code',
  `template` mediumtext NOT NULL COMMENT '模板内容',
  `create_time` varchar(24) DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`message_biz_type`,`message_send_type`),
  KEY `FK_Reference_12` (`message_send_type`,`message_biz_type`),
  CONSTRAINT `FK_Reference_12` FOREIGN KEY (`message_send_type`, `message_biz_type`) REFERENCES `message_send_type` (`message_send_type`, `message_biz_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='消息模板';

-- ----------------------------
-- Table structure for send_type
-- ----------------------------
DROP TABLE IF EXISTS `send_type`;
CREATE TABLE `send_type` (
  `message_send_type` varchar(10) NOT NULL COMMENT '发送类型code',
  `send_type_name` varchar(16) NOT NULL COMMENT '发送类型名称',
  PRIMARY KEY (`message_send_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='发送类型';

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `user_id` varchar(64) NOT NULL COMMENT '用户id',
  `user_name` varchar(64) DEFAULT NULL COMMENT '用户名',
  `create_time` varchar(24) DEFAULT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户';

-- ----------------------------
-- Table structure for user_address
-- ----------------------------
DROP TABLE IF EXISTS `user_address`;
CREATE TABLE `user_address` (
  `user_id` varchar(64) NOT NULL COMMENT '用户id',
  `message_send_type` varchar(10) NOT NULL COMMENT '发送类型code',
  `address` varchar(128) NOT NULL COMMENT '地址',
  `create_time` varchar(24) DEFAULT NULL,
  PRIMARY KEY (`user_id`,`message_send_type`),
  KEY `FK_Reference_11` (`message_send_type`),
  CONSTRAINT `FK_Reference_10` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`),
  CONSTRAINT `FK_Reference_11` FOREIGN KEY (`message_send_type`) REFERENCES `send_type` (`message_send_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户地址';

-- ----------------------------
-- Table structure for user_group
-- ----------------------------
DROP TABLE IF EXISTS `user_group`;
CREATE TABLE `user_group` (
  `user_id` varchar(64) NOT NULL COMMENT '用户id',
  `group_id` varchar(64) NOT NULL COMMENT '群组iid',
  `create_time` varchar(24) DEFAULT NULL,
  PRIMARY KEY (`user_id`,`group_id`),
  KEY `FK_Reference_8` (`group_id`),
  CONSTRAINT `FK_Reference_8` FOREIGN KEY (`group_id`) REFERENCES `groups` (`group_id`),
  CONSTRAINT `FK_Reference_9` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户群组关系';
