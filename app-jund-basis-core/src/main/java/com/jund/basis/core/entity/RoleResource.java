package com.jund.basis.core.entity;

import com.jund.framework.jpa.base.entity.BaseEntity;
import org.springframework.data.rest.core.annotation.Description;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author tanghui
 */
@Entity
@Table(name = "plt_sec_role_resource")
@Description("{title: '角色资源关系类'}")
public class RoleResource extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@Description("{title: '角色id'}")
	@Column(name = "role_id", nullable = false)
	private Long roleId;

	@Description("{title: '资源id'}")
	@Column(name = "res_id", nullable = false)
	private Long resId;

	public Long getRoleId() {
		return this.roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public Long getResId() {
		return resId;
	}

	public void setResId(Long resId) {
		this.resId = resId;
	}

	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		RoleResource pojo = (RoleResource) o;

		if (roleId != null ? !roleId.equals(pojo.roleId) : pojo.roleId != null)
			return false;
		if (resId != null ? !resId.equals(pojo.resId) : pojo.resId != null)
			return false;

		return true;
	}

	public int hashCode() {
		int result = 0;
		result = (roleId != null ? roleId.hashCode() : 0);
		result = 31 * result + (resId != null ? resId.hashCode() : 0);

		return result;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder(getClass().getSimpleName());

		sb.append(" [");
		sb.append("主键").append("：'").append(getId()).append("', ");
		sb.append("角色id").append("：'").append(getRoleId()).append("', ");
		sb.append("资源id").append("：'").append(getResId()).append("'");
		sb.append("]");

		return sb.toString();
	}

}
