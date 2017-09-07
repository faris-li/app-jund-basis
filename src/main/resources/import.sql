delete from  plt_sec_user_role;
delete from plt_sec_user_login;
delete from  plt_sec_user;
delete from  plt_sec_organ;
delete from  plt_sec_role;
delete from  plt_sec_pwd_rule;

INSERT INTO plt_sec_organ (id, org_code, org_level, org_name, org_seq, remark, status, pid) VALUES (1, '001', 0, '某某银行', '000', 'fdsg', 1, null);
INSERT INTO plt_sec_pwd_rule (id, min_length, max_length, reset_at_first_login, last_time_in, contains_az_up, contains_az_low, contains_09, contains_special_chars, contains_username, not_repeat_his_numbs, error_numbs, default_pwd)
 VALUES (1, 6, 9, 1, 180, 0, 0, 0, 0, 0, 2, 4, '111111');
INSERT INTO plt_sec_role (id, role_code, remark, role_name, adm_flag, priv_flag) VALUES (1, 'ROLE_sysadmin', 'sysadmin', 'sysadmin', 1, 0);
INSERT INTO plt_sec_role (id, role_code, remark, role_name, adm_flag, priv_flag) VALUES (2, 'ROLE_ADMIN', '能访问所有菜单资源', '系统管理员', 1, 0);
INSERT INTO plt_sec_user (id, email, emp_no, idcard, mobile_phone, office_phone, real_name, remark, status, user_name, org_id, priv_flag) VALUES (1, null, null, null, null, null, 'sysadmin', 'sysadmin', 1, 'sysadmin', 1, 1);
INSERT INTO plt_sec_user_login (id, his_pwd, is_first_login, last_login_time, locked_time, login_num, old_pwd, password, pwd_error_num, pwd_modify_time, user_flag, user_name)
 VALUES (1, 'b737c3cfaa1cb58e152238283a533d085e869766b5e4d95b2dec47b9bd5c30ac1abeb5a053e42cea,cebf71b9a1f4d19df40381cf3e3ef6650108f48aedb3ba13c97705f8151c37fff9d18bc73b1cd7c7', 1, null, null, null, 'cebf71b9a1f4d19df40381cf3e3ef6650108f48aedb3ba13c97705f8151c37fff9d18bc73b1cd7c7', 'cebf71b9a1f4d19df40381cf3e3ef6650108f48aedb3ba13c97705f8151c37fff9d18bc73b1cd7c7', null, '2017-07-10 10:24:34', 1, 'sysadmin');
INSERT INTO plt_sec_user_role (id, user_id, role_id) VALUES (1, 1, 1);
