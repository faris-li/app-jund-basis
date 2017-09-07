DROP TABLE if exists plt_dict_item;
DROP TABLE if exists plt_dict_type;
DROP TABLE if exists plt_param;
DROP TABLE if exists plt_sec_pwd_rule;
DROP TABLE if exists plt_sec_role_grp;
DROP TABLE if exists plt_sec_role_menu;
DROP TABLE if exists plt_sec_role_resource;
DROP TABLE if exists plt_sec_role_app;
DROP TABLE if exists plt_sec_user_role;
DROP TABLE if exists plt_sec_app;
DROP TABLE if exists plt_sec_menu;
DROP TABLE if exists plt_sec_role;
DROP TABLE if exists plt_sec_user_login;
DROP TABLE if exists plt_sec_user;
DROP TABLE if exists plt_sec_organ;


CREATE TABLE plt_dict_type (
	type_code varchar(64) NOT NULL COMMENT '字典类型编号',
	type_name varchar(128) NOT NULL COMMENT '类型名称',  
	module_code varchar(10) COMMENT '模块编号', 
	PRIMARY KEY (type_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='字典类型表';

CREATE TABLE plt_dict_item (
	type_code varchar(64) NOT NULL COMMENT '字典类型编号', 
	item_code varchar(32) NOT NULL COMMENT '字典项编号', 
	item_name varchar(120) NOT NULL COMMENT '字典名称', 
	sort_no int COMMENT '排序', 
	status int(1) DEFAULT 1 NOT NULL COMMENT '字典状态', 
	PRIMARY KEY (type_code, item_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='字典项表';


CREATE TABLE plt_param (
	param_code varchar(64) NOT NULL COMMENT '参数编号', 
	module_code varchar(10) COMMENT '模块编码', 
	param_value varchar(255) NOT NULL COMMENT '参数值', 
	remark varchar(255) COMMENT '备注', 
	PRIMARY KEY (param_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统参数';

CREATE TABLE plt_sec_app (
	id bigint NOT NULL AUTO_INCREMENT COMMENT '主键id', 
	app_code varchar(40) NOT NULL COMMENT '应用号', 
	app_name varchar(120) NOT NULL COMMENT '应用名称', 
	app_addr varchar(255) NOT NULL COMMENT '应用地址', 
	remark varchar(3000) COMMENT '备注', 
	status int(1) DEFAULT 1 NOT NULL COMMENT '状态', 
	app_icon varchar(3000) COMMENT '应用图标', 
	PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='应用表';

CREATE TABLE plt_sec_menu (
	id bigint NOT NULL AUTO_INCREMENT COMMENT '主键id',  
	menu_code varchar(40) NOT NULL COMMENT '菜单号', 
	menu_name varchar(120) NOT NULL COMMENT '菜单名', 
	menu_url varchar(255) COMMENT '菜单路径', 
	menu_icon varchar(255) COMMENT '菜单图标', 
	app_id bigint NOT NULL COMMENT '所属应用', 
	pid bigint COMMENT '父菜单', 
	sort_no int COMMENT '显示顺序', 
	status int(1) DEFAULT 1 NOT NULL COMMENT '生效标识', 
	remark varchar(255) COMMENT '备注', 
	expand_flag int(1) DEFAULT 0 COMMENT '是否展开', 
	menu_type int(1) COMMENT '菜单类型,0菜单组，1菜单', 
	PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='菜单表';

CREATE TABLE plt_sec_organ (
	id bigint NOT NULL AUTO_INCREMENT COMMENT '主键id', 
	org_code varchar(40) NOT NULL COMMENT '机构号', 
	org_level int(1) COMMENT '机构层级', 
	org_name varchar(120) NOT NULL COMMENT '机构名称', 
	org_seq varchar(30) NOT NULL COMMENT '机构序列', 
	remark varchar(255) COMMENT '备注', 
	status int(1) DEFAULT 1 NOT NULL COMMENT '状态', 
	pid bigint COMMENT '上级机构', 
	PRIMARY KEY (id), 
	INDEX FKlpdak15qwkapjbosusc7ebpx3 (pid)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='机构表';
ALTER TABLE `plt_sec_organ` ADD CONSTRAINT FKlpdak15qwkapjbosusc7ebpx1 FOREIGN KEY (`pid`) REFERENCES `plt_sec_organ` (`id`);

CREATE TABLE plt_sec_pwd_rule (
	id bigint NOT NULL AUTO_INCREMENT COMMENT '主键id', 
	min_length int NOT NULL COMMENT '密码最小长度', 
	max_length int NOT NULL COMMENT '密码最大长度', 
	reset_at_first_login int NOT NULL COMMENT '首次登陆是否修改密码', 
	last_time_in int NOT NULL COMMENT '密码修改间隔天数', 
	contains_az_up int(1) NOT NULL COMMENT '是否包含大写字母', 
	contains_az_low int(1) NOT NULL COMMENT '是否包含小写字母', 
	contains_09 int(1) NOT NULL COMMENT '是否包含数字', 
	contains_special_chars varchar(128) COMMENT '包含的特殊字符', 
	contains_username int(1) NOT NULL COMMENT '不允许包含用户名', 
	not_repeat_his_numbs int NOT NULL COMMENT '历史密码不允许重复的次数', 
	error_numbs int NOT NULL COMMENT '密码错误次数', 
	default_pwd varchar(40) NOT NULL COMMENT '默认密码', 
	PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT '密码策略';

CREATE TABLE plt_sec_role (
	id bigint NOT NULL AUTO_INCREMENT COMMENT '主键id', 
	role_code varchar(40) NOT NULL COMMENT '角色号', 
	role_name varchar(120) NOT NULL COMMENT '角色名称', 
	remark varchar(255) COMMENT '描述', 
	adm_flag int(1) NOT NULL DEFAULT 0 COMMENT '管理员标识', 
	priv_flag int(1) NOT NULL COMMENT '是否授权角色', 
	PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色表';

CREATE TABLE plt_sec_role_grp (
	id bigint NOT NULL AUTO_INCREMENT COMMENT '主键id', 
	sup_role_id bigint NOT NULL COMMENT '父角色id', 
	sub_role_id bigint NOT NULL COMMENT '子角色id', 
	PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT '角色分组';

CREATE TABLE plt_sec_role_menu (
	id bigint NOT NULL AUTO_INCREMENT COMMENT '主键id', 
	menu_id bigint NOT NULL COMMENT '菜单id', 
	role_id bigint NOT NULL COMMENT '角色id', 
	PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT '角色菜单关系表';

CREATE TABLE plt_sec_role_resource (
	id bigint NOT NULL AUTO_INCREMENT COMMENT '主键id',  
	res_id bigint NOT NULL COMMENT '资源id', 
	role_id bigint NOT NULL COMMENT '角色id', 
	PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT '角色资源关系表';

CREATE TABLE plt_sec_role_app (
	id bigint NOT NULL AUTO_INCREMENT COMMENT '主键id',  
	app_id bigint NOT NULL COMMENT '应用id', 
	role_id bigint NOT NULL COMMENT '角色id', 
	PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT '角色应用关系表';

CREATE TABLE plt_sec_user (
	id bigint NOT NULL AUTO_INCREMENT COMMENT '主键id', 
	user_name varchar(40) NOT NULL COMMENT '用户账号', 
	real_name varchar(40) NOT NULL COMMENT '用户姓名', 
	email varchar(64) COMMENT '邮箱', 
	emp_no varchar(40) COMMENT '员工编号',
	idcard varchar(18) COMMENT '身份证号',
	mobile_phone varchar(20) COMMENT '移动电话',
	office_phone varchar(20) COMMENT '办公电话', 
	remark varchar(255) COMMENT '备注', 
	org_id bigint NOT NULL COMMENT '所属机构', 
	priv_flag int(1) NOT NULL COMMENT '是否授权角色', 
	status int(1) default 1 NOT NULL COMMENT '生效标识', 
	PRIMARY KEY (id), 
	INDEX FK59bmecpxdoti6bh1hnsaqaq4j (org_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
ALTER TABLE `plt_sec_user` ADD CONSTRAINT FK59bmecpxdoti6bh1hnsaqaq4j FOREIGN KEY (`org_id`) REFERENCES `plt_sec_organ` (`id`);

CREATE TABLE plt_sec_user_login (
	id bigint NOT NULL AUTO_INCREMENT COMMENT '主键id', 
	his_pwd varchar(4000) COMMENT '历史密码',
	is_first_login int(1) COMMENT '是否首次登录', 
	last_login_time datetime COMMENT '最后登录时间',
	locked_time datetime COMMENT '用户锁定时间',
	login_num bigint COMMENT '登录次数',
	old_pwd varchar(128) COMMENT '上次密码',
	password varchar(128) NOT NULL COMMENT '密码',
	pwd_error_num int COMMENT '密码错误次数',
	pwd_modify_time datetime COMMENT '密码修改时间',
	user_flag int(1) NOT NULL COMMENT '启用&锁定状态',
	user_name varchar(40) NOT NULL COMMENT '用户名',
	PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT '用户登陆信息类';


CREATE TABLE plt_sec_user_role (
	id bigint NOT NULL AUTO_INCREMENT COMMENT '主键id',
	user_id bigint NOT NULL COMMENT '用户id',   
	role_id bigint NOT NULL COMMENT '角色id',   
	PRIMARY KEY (id), 
	INDEX FKaklj7smi4o5vvojkh1qr8mtne (role_id), 
	INDEX FKmp8pwg52lnr1d6dsf3pr7ud6m (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT '用户角色关系表';
ALTER TABLE `plt_sec_user_role` ADD CONSTRAINT FKmp8pwg52lnr1d6dsf3pr7ud6m FOREIGN KEY (`user_id`) REFERENCES `plt_sec_user` (`id`);
ALTER TABLE `plt_sec_user_role` ADD CONSTRAINT FKaklj7smi4o5vvojkh1qr8mtne FOREIGN KEY (`role_id`) REFERENCES `plt_sec_role` (`id`);

DROP TABLE plt_log;
CREATE TABLE plt_log (
	id bigint NOT NULL AUTO_INCREMENT COMMENT '主键id',
	log_title varchar(255) NOT NULL COMMENT '日志标题', 
	opt_time datetime NOT NULL COMMENT '操作时间', 
	log_data varchar(3000) COMMENT '日志数据,如a:11,b:"abcdef"', 
	client_ip varchar(50) NOT NULL COMMENT '客户端IP地址',	
	log_type int NOT NULL COMMENT '日志类型，如1业务和0异常', 	
	user_id bigint NOT NULL COMMENT '操作用户id', 
	user_name varchar(120) COMMENT '操作用户登录名', 
	PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT '操作日志表';

