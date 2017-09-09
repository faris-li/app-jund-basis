package com.jund.basic.core.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.jund.framework.jpa.base.entity.BaseEntity;
import org.springframework.data.rest.core.annotation.Description;

/**
 * @author tanghui
 */
@Entity
@Table(name = "plt_sec_role")
@Description("{title: '角色实体类'}")
public class Role extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@Description("{title: '角色号'}")
	@Column(name = "role_code", nullable = false, length = 40)
	private String roleCode;

	@Description("{title: '角色名称'}")
	@Column(name = "role_name", nullable = false, length = 40)
	private String roleName;

	@Description("{title: '描述'}")
	@Column(name = "remark", length = 85)
	private String remark;

	@Description("{title: '管理员标识'}")
	@Column(name = "adm_flag", nullable = false, length = 1)
	private Integer admFlag;

	@Description("{title: '是否授权角色'}")
	@Column(name = "priv_flag", nullable = false, length = 1)
	private Integer privFlag;

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
		return this.remark;
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

	public void setId(Long id) {
		this.id = id;
	}

	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		Role pojo = (Role) o;

		if (id != null ? !id.equals(pojo.id) : pojo.id != null)
			return false;
		if (roleCode != null ? !roleCode.equals(pojo.roleCode) : pojo.roleCode != null)
			return false;

		return true;
	}

	public int hashCode() {
		int result = 0;
		result = (roleCode != null ? roleCode.hashCode() : 0);

		return result;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder(getClass().getSimpleName());

		sb.append(" [");
		sb.append("角色主键").append("：'").append(getId()).append("', ");
		sb.append("角色编码").append("：'").append(getRoleCode()).append("', ");
		sb.append("角色名称").append("：'").append(getRoleName()).append("', ");
		sb.append("角色描述").append("：'").append(getRemark()).append("', ");
		sb.append("]");

		return sb.toString();
	}

}
