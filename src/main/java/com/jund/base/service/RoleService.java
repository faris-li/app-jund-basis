package com.jund.base.service;

import com.jund.framework.jpa.base.service.BaseService;
import com.jund.platformwork.security.model.Role;
import com.jund.platformwork.security.model.dto.RoleDTO;
import com.jund.platformwork.security.model.dto.RoleForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RoleService extends BaseService<Role, Long> {

	/**
	 * 查询指定用户直接拥有的角色
	 * @param userName
	 * @return
	 */
	List<Role> findRoleByUser(String userName);

	/**
	 * 查询指定用户所管理的角色, 除了指定的角色
	 * @param currUserName
	 * @return
	 */
	List<Role> findUserMgrRole(String currUserName);

	/**
	 * 查询指定角色的下级角色
	 * @param roleId
	 * @return
	 */
	List<Role> findSubRoleByRole(Long roleId);

	/**
	 * 角色保存，同时保存被管理角色
	 * @param role
	 * @param appIds 应用
	 * @param menuIds 菜单
	 * @param subIds 可管理角色
	 * @param supId 上级角色
	 */
	void save(Role role, Long[] appIds, Long[] menuIds, Long[] subIds, Long supId);

	/**
	 * 分页查询角色及下级角色
	 * @param currUserName
	 * @param RoleForm
	 * @param pageable
	 * @return
	 */
	Page<RoleDTO> findRoleAndSubByCurrUser(String currUserName, RoleForm RoleForm, Pageable pageable);
	
	/**
	 * 根据roleCode查询角色
	 * @param roleCode
	 * @return
	 */
	Role findByRoleCode(String roleCode);
}
