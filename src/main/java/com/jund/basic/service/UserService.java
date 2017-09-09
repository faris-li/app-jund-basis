package com.jund.basic.service;

import com.jund.framework.jpa.base.service.BaseService;
import com.jund.platformwork.security.model.User;
import com.jund.platformwork.security.model.dto.UserDTO;
import com.jund.platformwork.security.model.dto.UserForm;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface UserService extends BaseService<User, Long> {

	/**
	 * 判断用户是否可以注销
	 * @param userIds
	 * @return
	 */
	boolean isCancelUsing(Long[] userIds);

	/**
	 * 注销用户
	 * @param userIds
	 */
	void cancelUsing(Long[] userIds);
	
	/**
	 * 根据用户名查询用户
	 * @param username
	 * @return
	 */
	User findUserByUserName(String username);
	
	/**
	 * 根据角色查询授权用户
	 * @param roleId
	 * @return
	 */
	List<User> findUserByRole(Long roleId);
	
	/**
	 * 用户保存
	 * @param user
	 */
	void save(User user, Long orgId, Long[] roleIds);
	
	/**
	 * 分页查询用户
	 * @param currUserName
	 * @param user
	 * @param pageable
	 * @return
	 */
	Page<UserDTO> findAllUser(String currUserName, UserForm user, Pageable pageable);
	
	/**
	 * 导出
	 * @param currUserName
	 * @return
	 */
	Workbook exp(String currUserName);

	/**
	 * 批量导入用户及用户权限
	 * @param dataResult
	 * @param currUserName
	 */
	void batchsave(List<Map<String, Object>> dataResult, String currUserName);
}
