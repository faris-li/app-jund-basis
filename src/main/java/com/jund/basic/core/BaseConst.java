package com.jund.basic.core;


public abstract class BaseConst extends com.jund.framework.core.Constants{
	
	/**
	 * 密码验证规则
	 */
	public static final String SYS_SEC_PWD_LOCK_TRIGGER = "sys_sec_pwd_lock_trigger";
	public static final String SYS_SEC_PWD_MODIFY_TRIGGER = "sys_sec_pwd_modify_trigger";
	public static final String SYS_SEC_PWD_MODIFY_STRATEGY = "sys_sec_pwd_modify_strategy";
	public static final String ERROR_NUMBS = "errorNumbs";
	public static final String LAST_TIME_IN = "lastTimeIn";
	public static final String AT_FIRST_LOGIN = "atFirstLogin";
	
	public static final String STR_MIN_LENGTH = "str_min_length";
	public static final String STR_MAX_LENGTH = "str_max_length";
	public static final String STR_CONTENT_AZ = "str_content_AZ";
	public static final String STR_CONTENT_az = "str_content_az";
	public static final String STR_CONTENT_09 = "str_content_09";
	public static final String STR_CONTENT_SPECIAL_CHAR = "str_content_special_char";
	public static final String STR_NOT_USERNAME = "str_not_username";
	public static final String STR_NOT_HIS_NUMBS = "str_not_his_numbs";
	public static final String STR_PASSWORD = "password";
	
	public static final String MATCH_STR_CONTENT_AZ = ".*[A-Z]+.*";
	public static final String MATCH_STR_CONTENT_az = ".*[a-z]+.*";
	public static final String MATCH_STR_CONTENT_09 = ".*[0-9]+.*";

	public static final String MATCH_STR_LENGTH = "^.{min,max}$";
	public static final String MATCH_STR_CONTENT_SPECIAL_CHAR = ".*[special_char]+.*";
	public static final String MATCH_STR_NOT_USERNAME = "^((?!username).)*$";
	public static final String MATCH_STR_NOT_HIS_NUMBS = "^((?!pwd).)*$";
	
	public static final String MESSAGE_MATCH_STR_LENGTH = "密码长度应在min至max之间";
	public static final String MESSAGE_MATCH_STR_CONTENT_AZ = "密码至少包含A-Z中任一大写字母";
	public static final String MESSAGE_MATCH_STR_CONTENT_az = "密码至少包含a-z中任一小写字母";
	public static final String MESSAGE_MATCH_STR_CONTENT_09 = "密码至少包含0-9中任一数字";
	public static final String MESSAGE_MATCH_STR_CONTENT_SPECIAL_CHAR = "密码至少包含special_char任一字符";
	public static final String MESSAGE_MATCH_STR_NOT_USERNAME = "密码不能包含用户名";
	public static final String MESSAGE_MATCH_STR_NOT_HIS_NUMBS = "密码不能与前n次重复";
	
	public static final String PARAM_ROLE_ADMIN_DISPLAY_FLAG = "plt_role_admin_display_flag";
	public static final String PARAM_LOG_FLAG = "plt_log_flag";
	public static final String ADMIN_FLG_TRUE = "1";
	
	public static final Integer type_inner = 0;
	public static final Integer type_next = 1;
	public static final Integer type_prev = 2;
	
}
