package com.jund.base.entity.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RoleDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7384126108387014408L;
	private Long id; // 角色编号
	private String roleCode; // 角色编码
	private String roleName; // 角色名称
	private String remark; // 角色描述
	private Long userId; // 用户ID

	private Long supId; // 上级角色
	// private String mutexId; // 互斥角色ID

	private List<RoleDTO> subRoles = new ArrayList<RoleDTO>(); // 可管理角色

	public RoleDTO(Long id, String roleName, Long supId) {
		super();
		this.id = id;
		this.roleName = roleName;
		this.supId = supId;
	}

	public RoleDTO() {
		super();
	}

	// private List<RoleDTO> roleMutexs = new ArrayList<RoleDTO>(); //互斥角色
	//
	// public List<RoleDTO> getRoleMutesx() {
	// return roleMutexs;
	// }
	// public void setRoleMutex(List<RoleDTO> roleMutexs) {
	// this.roleMutexs = roleMutexs;
	// }
	// public String getMutexId() {
	// return mutexId;
	// }
	// public void setMutexId(String mutexId) {
	// this.mutexId = mutexId;
	// }
	public Long getSupId() {
		return supId;
	}

	public void setSupId(Long supId) {
		this.supId = supId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public List<RoleDTO> getSubRoles() {
		return subRoles;
	}

	public void setSubRoles(List<RoleDTO> subRoles) {
		this.subRoles = subRoles;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRoleCode() {
		return roleCode;
	}

	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

}
