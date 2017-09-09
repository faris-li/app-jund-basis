package com.jund.basic.core.entity.dto;

import java.io.Serializable;

/**
 * @author tanghui
 */
public class RoleForm implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;

	private String roleCode;

	private String roleName;

	private String remark;

	private Integer admFlag;

	private Integer privFlag;

	private Long[] subIds;

	private Long[] menuIds;
	
	private Long[] appIds;

	private Long supId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRoleCode() {
		return this.roleCode;
	}

	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}

	public String getRoleName() {
		return this.roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getAdmFlag() {
		return admFlag;
	}

	public void setAdmFlag(Integer admFlag) {
		this.admFlag = admFlag;
	}

	public Integer getPrivFlag() {
		return privFlag;
	}

	public void setPrivFlag(Integer privFlag) {
		this.privFlag = privFlag;
	}

	public Long[] getSubIds() {
		return subIds;
	}

	public void setSubIds(Long[] subIds) {
		this.subIds = subIds;
	}

	public Long[] getMenuIds() {
		return menuIds;
	}

	public void setMenuIds(Long[] menuIds) {
		this.menuIds = menuIds;
	}

	public Long getSupId() {
		return supId;
	}

	public void setSupId(Long supId) {
		this.supId = supId;
	}

	public Long[] getAppIds() {
		return appIds;
	}

	public void setAppIds(Long[] appIds) {
		this.appIds = appIds;
	}

}
