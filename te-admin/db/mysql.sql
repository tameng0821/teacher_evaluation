-- 菜单
CREATE TABLE `sys_menu` (
  `menu_id` bigint NOT NULL AUTO_INCREMENT,
  `parent_id` bigint COMMENT '父菜单ID，一级菜单为0',
  `name` varchar(50) COMMENT '菜单名称',
  `url` varchar(200) COMMENT '菜单URL',
  `perms` varchar(500) COMMENT '授权(多个用逗号分隔，如：user:list,user:create)',
  `type` int COMMENT '类型   0：目录   1：菜单   2：按钮',
  `icon` varchar(50) COMMENT '菜单图标',
  `order_num` int COMMENT '排序',
  PRIMARY KEY (`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='菜单管理';

-- 部门
CREATE TABLE `sys_dept` (
  `dept_id` bigint NOT NULL AUTO_INCREMENT,
  `parent_id` bigint COMMENT '上级部门ID，一级部门为0',
  `name` varchar(50) COMMENT '部门名称',
  `order_num` int COMMENT '排序',
  `del_flag` tinyint DEFAULT 0 COMMENT '是否删除  -1：已删除  0：正常',
  PRIMARY KEY (`dept_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='部门管理';

-- 系统用户
CREATE TABLE `sys_user` (
  `user_id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL COMMENT '用户名/工号',
  `password` varchar(100) COMMENT '密码',
  `salt` varchar(20) COMMENT '盐',
  `name` varchar(100) COMMENT '姓名',
  `email` varchar(100) COMMENT '邮箱',
  `mobile` varchar(100) COMMENT '手机号',
  `status` tinyint COMMENT '状态  0：禁用   1：正常',
  `dept_id` bigint(20) COMMENT '部门ID',
  `create_time` datetime COMMENT '创建时间',
  PRIMARY KEY (`user_id`),
  UNIQUE INDEX (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统用户';

-- 角色
CREATE TABLE `sys_role` (
  `role_id` bigint NOT NULL AUTO_INCREMENT,
  `role_name` varchar(100) COMMENT '角色名称',
  `remark` varchar(100) COMMENT '备注',
  `dept_id` bigint(20) COMMENT '部门ID',
  `create_time` datetime COMMENT '创建时间',
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色';

-- 用户与角色对应关系
CREATE TABLE `sys_user_role` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint COMMENT '用户ID',
  `role_id` bigint COMMENT '角色ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户与角色对应关系';

-- 角色与菜单对应关系
CREATE TABLE `sys_role_menu` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `role_id` bigint COMMENT '角色ID',
  `menu_id` bigint COMMENT '菜单ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色与菜单对应关系';

-- 角色与部门对应关系
CREATE TABLE `sys_role_dept` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `role_id` bigint COMMENT '角色ID',
  `dept_id` bigint COMMENT '部门ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色与部门对应关系';


-- 系统配置信息
CREATE TABLE `sys_config` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `param_key` varchar(50) COMMENT 'key',
  `param_value` varchar(2000) COMMENT 'value',
  `status` tinyint DEFAULT 1 COMMENT '状态   0：隐藏   1：显示',
  `remark` varchar(500) COMMENT '备注',
  PRIMARY KEY (`id`),
  UNIQUE INDEX (`param_key`)
) ENGINE=`InnoDB` DEFAULT CHARACTER SET utf8 COMMENT='系统配置信息表';

-- 数据字典
CREATE TABLE `sys_dict` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL COMMENT '字典名称',
  `type` varchar(100) NOT NULL COMMENT '字典类型',
  `code` varchar(100) NOT NULL COMMENT '字典码',
  `value` varchar(1000) NOT NULL COMMENT '字典值',
  `order_num` int DEFAULT 0 COMMENT '排序',
  `remark` varchar(255) COMMENT '备注',
  `del_flag` tinyint DEFAULT 0 COMMENT '删除标记  -1：已删除  0：正常',
  PRIMARY KEY (`id`),
  UNIQUE KEY(`type`,`code`)
) ENGINE=InnoDB DEFAULT CHARACTER SET utf8 COMMENT='数据字典表';

-- 系统日志
CREATE TABLE `sys_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) COMMENT '用户名',
  `operation` varchar(50) COMMENT '用户操作',
  `method` varchar(200) COMMENT '请求方法',
  `params` varchar(5000) COMMENT '请求参数',
  `time` bigint NOT NULL COMMENT '执行时长(毫秒)',
  `ip` varchar(64) COMMENT 'IP地址',
  `create_date` datetime COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=`InnoDB` DEFAULT CHARACTER SET utf8 COMMENT='系统日志';

-- 初始数据
INSERT INTO `sys_user` (`user_id`, `username`, `password`, `salt`,`name`,`email`, `mobile`, `status`, `dept_id`, `create_time`) VALUES ('1', 'admin', 'e1153123d7d180ceeb820d577ff119876678732a68eef4e6ffc0b1f06a01f91b', 'YzcmCZNvbXocrsz9dm8e','管理员', 'root@lyu.edu.cn', '13612345678', '1', '1', '2019-5-1 11:11:11');
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES ('1', '0', '系统管理', NULL, NULL, '0', 'fa fa-cog', '0');
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES ('2', '1', '用户管理', 'modules/sys/user.html', NULL, '1', 'fa fa-user', '1');
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES ('3', '1', '角色管理', 'modules/sys/role.html', NULL, '1', 'fa fa-user-secret', '2');
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES ('4', '1', '菜单管理', 'modules/sys/menu.html', NULL, '1', 'fa fa-th-list', '3');
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES ('5', '1', 'SQL监控', 'druid/sql.html', NULL, '1', 'fa fa-bug', '4');
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES ('15', '2', '查看', NULL, 'sys:user:list,sys:user:info', '2', NULL, '0');
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES ('16', '2', '新增', NULL, 'sys:user:save,sys:role:select', '2', NULL, '0');
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES ('17', '2', '修改', NULL, 'sys:user:update,sys:role:select', '2', NULL, '0');
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES ('18', '2', '删除', NULL, 'sys:user:delete', '2', NULL, '0');
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES ('19', '3', '查看', NULL, 'sys:role:list,sys:role:info', '2', NULL, '0');
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES ('20', '3', '新增', NULL, 'sys:role:save,sys:menu:perms', '2', NULL, '0');
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES ('21', '3', '修改', NULL, 'sys:role:update,sys:menu:perms', '2', NULL, '0');
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES ('22', '3', '删除', NULL, 'sys:role:delete', '2', NULL, '0');
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES ('23', '4', '查看', NULL, 'sys:menu:list,sys:menu:info', '2', NULL, '0');
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES ('24', '4', '新增', NULL, 'sys:menu:save,sys:menu:select', '2', NULL, '0');
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES ('25', '4', '修改', NULL, 'sys:menu:update,sys:menu:select', '2', NULL, '0');
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES ('26', '4', '删除', NULL, 'sys:menu:delete', '2', NULL, '0');
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES ('27', '1', '参数管理', 'modules/sys/config.html', 'sys:config:list,sys:config:info,sys:config:save,sys:config:update,sys:config:delete', '1', 'fa fa-sun-o', '6');
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES ('29', '1', '系统日志', 'modules/sys/log.html', 'sys:log:list', '1', 'fa fa-file-text-o', '7');
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES ('31', '1', '部门管理', 'modules/sys/dept.html', NULL, '1', 'fa fa-file-code-o', '1');
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES ('32', '31', '查看', NULL, 'sys:dept:list,sys:dept:info', '2', NULL, '0');
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES ('33', '31', '新增', NULL, 'sys:dept:save,sys:dept:select', '2', NULL, '0');
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES ('34', '31', '修改', NULL, 'sys:dept:update,sys:dept:select', '2', NULL, '0');
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES ('35', '31', '删除', NULL, 'sys:dept:delete', '2', NULL, '0');

INSERT INTO `sys_menu`(`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (36, 1, '字典管理', 'modules/sys/dict.html', NULL, 1, 'fa fa-bookmark-o', 6);
INSERT INTO `sys_menu`(`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (37, 36, '查看', NULL, 'sys:dict:list,sys:dict:info', 2, NULL, 6);
INSERT INTO `sys_menu`(`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (38, 36, '新增', NULL, 'sys:dict:save', 2, NULL, 6);
INSERT INTO `sys_menu`(`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (39, 36, '修改', NULL, 'sys:dict:update', 2, NULL, 6);
INSERT INTO `sys_menu`(`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (40, 36, '删除', NULL, 'sys:dict:delete', 2, NULL, 6);


INSERT INTO `sys_dept` (`dept_id`, `parent_id`, `name`, `order_num`, `del_flag`) VALUES ('1', '0', '系统管理员', '0', '0');

INSERT INTO `sys_dept` (`dept_id`, `parent_id`, `name`, `order_num`, `del_flag`) VALUES ('2', '0', '临沂大学', '0', '0');
INSERT INTO `sys_dept` (`dept_id`, `parent_id`, `name`, `order_num`, `del_flag`) VALUES ('3', '2', '信息科学与工程学院', '0', '0');


INSERT INTO `sys_dict`(`id`, `name`, `type`, `code`, `value`, `order_num`, `remark`, `del_flag`) VALUES (1, '性别', 'sex', '0', '女', 0, NULL, 0);
INSERT INTO `sys_dict`(`id`, `name`, `type`, `code`, `value`, `order_num`, `remark`, `del_flag`) VALUES (2, '性别', 'sex', '1', '男', 1, NULL, 0);
INSERT INTO `sys_dict`(`id`, `name`, `type`, `code`, `value`, `order_num`, `remark`, `del_flag`) VALUES (3, '性别', 'sex', '2', '未知', 3, NULL, 0);


/*==============================================================*/
/* 教师评价系统                                  */
/*==============================================================*/

create table tb_eval_base_item
(
   id                   bigint not null AUTO_INCREMENT,
   name                 varchar(100) not null COMMENT '名称',
   percentage           int not null COMMENT '百分比',
   remark               varchar(500) COMMENT '备注',
   primary key (id)
)ENGINE=`InnoDB` DEFAULT CHARACTER SET utf8 COMMENT='评分标准基础项目';

create table tb_colleague_eval_base_item
(
   id                   bigint not null AUTO_INCREMENT,
   name                 varchar(100) not null COMMENT '名称',
   percentage           int not null COMMENT '百分比',
   remark               varchar(500) COMMENT '备注',
   primary key (id)
)ENGINE=`InnoDB` DEFAULT CHARACTER SET utf8 COMMENT='同行评价基础项目';

create table tb_inspector_eval_base_item
(
   id                   bigint not null AUTO_INCREMENT,
   name                 varchar(100) not null COMMENT '名称',
   percentage           int not null COMMENT '百分比',
   remark               varchar(500) COMMENT '备注',
   primary key (id)
)ENGINE=`InnoDB` DEFAULT CHARACTER SET utf8 COMMENT='督导评价基础项目';

INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES ('100', '0', '评价管理', NULL, NULL, '0', 'fa fa-cog', '0');
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES ('110', '100', '评价标准', 'modules/eval/evalbaseitem.html', NULL, '1', 'fa fa-bandcamp', '1');
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES ('111', '100', '同行评价项目', 'modules/eval/colleagueevalbaseitem.html', NULL, '1', 'fa fa-bandcamp', '2');
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES ('112', '100', '督导评价项目', 'modules/eval/inspectorevalbaseitem.html', NULL, '1', 'fa fa-bandcamp', '3');

INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES ('120', '110', '查看', null, 'eval:evalbaseitem:list,eval:evalbaseitem:info', '2', null, '1');
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES ('121', '110', '修改', null, 'eval:evalbaseitem:update', '2', null, '3');

INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES ('125', '111', '查看', null, 'eval:colleagueevalbaseitem:list,eval:colleagueevalbaseitem:info', '2', null, '1');
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES ('126', '111', '新增', null, 'eval:colleagueevalbaseitem:save', '2', null, '2');
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES ('127', '111', '修改', null, 'eval:colleagueevalbaseitem:update', '2', null, '3');
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES ('128', '111', '删除', null, 'eval:colleagueevalbaseitem:delete', '2', null, '4');

INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES ('131', '112', '查看', null, 'eval:inspectorevalbaseitem:list,eval:inspectorevalbaseitem:info', '2', null, '1');
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES ('132', '112', '新增', null, 'eval:inspectorevalbaseitem:save', '2', null, '2');
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES ('133', '112', '修改', null, 'eval:inspectorevalbaseitem:update', '2', null, '3');
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES ('134', '112', '删除', null, 'eval:inspectorevalbaseitem:delete', '2', null, '4');


create table tb_eval_task
(
   id                   bigint not null AUTO_INCREMENT,
   name                 varchar(100) not null COMMENT '名称',
   create_time          datetime COMMENT '创建时间',
   status               tinyint COMMENT '状态，0：新建；1：发布；2：关闭；',
   dept_id              bigint COMMENT '评价部门ID',
   remark               varchar(500) COMMENT '备注',
   headcount               int COMMENT '参评总人数',
   primary key (id)
)ENGINE=`InnoDB` DEFAULT CHARACTER SET utf8 COMMENT='评价任务';

create table tb_student_eval_task
(
   id                   bigint not null AUTO_INCREMENT,
   task_id              bigint COMMENT '任务ID',
   percentage  int COMMENT '评价占比',
   primary key (id)
)ENGINE=`InnoDB` DEFAULT CHARACTER SET utf8 COMMENT='学生评价子任务';

create table tb_colleague_eval_task
(
   id                   bigint not null AUTO_INCREMENT,
   task_id              bigint  COMMENT '任务ID',
   dept_id              bigint  COMMENT '部门ID',
   user_id              bigint  COMMENT '系主任ID',
   percentage  int COMMENT '评价占比',
   primary key (id)
)ENGINE=`InnoDB` DEFAULT CHARACTER SET utf8 COMMENT='同行/系主任评价子任务';

create table tb_colleague_eval_task_item
(
   id                   bigint not null AUTO_INCREMENT,
   task_id              bigint COMMENT '任务ID',
   name                 varchar(100) COMMENT '评价项目名称',
   percentage           int COMMENT '评价占比',
   remark               varchar(500) COMMENT '项目备注',
   primary key (id)
)ENGINE=`InnoDB` DEFAULT CHARACTER SET utf8 COMMENT='同行评价子任务评价项目';

create table tb_inspector_eval_task
(
   id                   bigint not null AUTO_INCREMENT,
   task_id              bigint COMMENT '任务ID',
   user_id              bigint COMMENT '督导ID',
   percentage  int COMMENT '评价占比',
   primary key (id)
)ENGINE=`InnoDB` DEFAULT CHARACTER SET utf8 COMMENT='督导评价子任务';

create table tb_inspector_eval_task_item
(
   id                   bigint not null AUTO_INCREMENT,
   task_id              bigint COMMENT '任务ID',
   name                 varchar(100) COMMENT '评价项目名称',
   percentage           int COMMENT '评价占比',
   remark               varchar(500) COMMENT '项目备注',
   primary key (id)
)ENGINE=`InnoDB` DEFAULT CHARACTER SET utf8 COMMENT='督导评价子任务评价项目';

create table tb_other_eval_task
(
   id                   bigint not null AUTO_INCREMENT,
   task_id              bigint COMMENT '任务ID',
   percentage  int not null COMMENT '评价占比',
   primary key (id)
)ENGINE=`InnoDB` DEFAULT CHARACTER SET utf8 COMMENT='其他评价子任务';


INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES ('140', '100', '评价任务', 'modules/eval/evaltask.html', NULL, '1', 'fa fa-tasks', '4');
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES ('141', '140', '查看', null, 'eval:evaltask:list,eval:evaltask:info', '2', null, '1');
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES ('142', '140', '创建', null, 'eval:evaltask:save', '2', null, '2');
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES ('143', '140', '修改', null, 'eval:evaltask:update', '2', null, '3');
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES ('144', '140', '发布', null, 'eval:evaltask:release', '2', null, '4');
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES ('145', '140', '关闭', null, 'eval:evaltask:close', '2', null, '5');
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES ('146', '140', '删除', null, 'eval:evaltask:delete', '2', null, '6');
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES ('147', '140', '生成结果', null, 'eval:evaltask:result', '2', null, '7');

INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES ('150', '100', '学生评价', 'modules/eval/studentevaltask.html', 'eval:studentevaltask:eval', '1', 'fa fa-graduation-cap', '5');
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES ('155', '100', '同行评价', 'modules/eval/colleagueevaltask.html', 'eval:colleagueevaltask:eval', '1', 'fa fa-user-o', '6');
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES ('160', '100', '督导评价', 'modules/eval/inspectorevaltask.html', 'eval:inspectorevaltask:eval', '1', 'fa fa-eye', '7');
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES ('165', '100', '其他评价', 'modules/eval/otherevaltask.html', 'eval:otherevaltask:eval', '1', 'fa fa-check-circle', '8');

INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES ('170', '100', '评价结果', 'modules/eval/evalresult.html', NULL, '1', 'fa fa-book', '9');
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES ('171', '170', '查看', null, 'eval:evalresult:list,eval:evalresult:info', '2', null, '1');
-- INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES ('172', '170', '修改', null, 'eval:evalresult:update', '2', null, '2');

create table tb_student_eval_record
(
   id                   bigint not null AUTO_INCREMENT,
   sub_task_id          bigint COMMENT '学生评价ID',
   user_id              bigint COMMENT '用户ID',
   score                double COMMENT '分数',
   update_time          datetime COMMENT '修改时间',
   remark               varchar(500) COMMENT '备注',
   primary key (id)
)ENGINE=`InnoDB` DEFAULT CHARACTER SET utf8 COMMENT='学生评价记录';

create table tb_colleague_eval_record
(
   id                   bigint not null AUTO_INCREMENT,
   sub_task_id          bigint COMMENT '同行评价ID',
   user_id              bigint COMMENT '用户ID',
   detail               varchar(500) COMMENT '记录每个评价选项以及分数，格式：taskItemID:taskItemScore,taskItemID1:taskItemScore1',
   score                double COMMENT '汇总分数',
   update_time          datetime COMMENT '更新时间',
   remark               varchar(500) COMMENT '备注',
   primary key (id)
)ENGINE=`InnoDB` DEFAULT CHARACTER SET utf8 COMMENT='同行评价记录';

create table tb_inspector_eval_record
(
   id                   bigint not null AUTO_INCREMENT,
   sub_task_id          bigint  COMMENT '督导评价ID',
   user_id              bigint COMMENT '用户ID',
   detail               varchar(500) COMMENT '记录每个评价选项以及分数，格式：taskItemID:taskItemScore,taskItemID1:taskItemScore1',
   score                double COMMENT '汇总分数',
   update_time          datetime COMMENT '更新时间',
   remark               varchar(500) COMMENT '备注',
   primary key (id)
)ENGINE=`InnoDB` DEFAULT CHARACTER SET utf8 COMMENT='督导评价记录';

create table tb_other_eval_record
(
   id                   bigint not null AUTO_INCREMENT,
   sub_task_id          bigint COMMENT '其他评价ID',
   user_id              bigint COMMENT '用户ID',
   score                double COMMENT '汇总分数',
   update_time          datetime COMMENT '更新时间',
   remark               varchar(500) COMMENT '备注',
   primary key (id)
)ENGINE=`InnoDB` DEFAULT CHARACTER SET utf8 COMMENT='其他评价记录';


create table tb_eval_result
(
   id                   bigint not null AUTO_INCREMENT,
   task_id              bigint COMMENT '关联评价任务ID',
   `username` varchar(50) NOT NULL COMMENT '用户名/工号',
   `name` varchar(100) COMMENT '姓名',
   `dept_name` varchar(50) COMMENT '系部名称',
   student_eval_score   double COMMENT '学生评价分数',
   student_eval_detail  varchar(500) COMMENT '学生评价细节',
   colleague_eval_score double COMMENT '同行评价分数',
   colleague_eval_detail varchar(500) COMMENT '同行评价细节',
   inspector_eval_score double COMMENT '督导评价分数',
   inspector_eval_detail varchar(500) COMMENT '督导评价细节',
   other_eval_score     double COMMENT '其他评价分数',
   other_eval_detail    varchar(500) COMMENT '其他评价细节',
   account_score        double comment '总分',
   ranking              bigint comment '排名',
   rating               varchar(20) comment '评级：30%为优秀，30~70为良好，70~100为合格，小于60分为不合格',
   remark               varchar(500) COMMENT '备注',
   update_time          datetime COMMENT '修改时间',
   primary key (id)
)ENGINE=`InnoDB` DEFAULT CHARACTER SET utf8 COMMENT='评价结果';
