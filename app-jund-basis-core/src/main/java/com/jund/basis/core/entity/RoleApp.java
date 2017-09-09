package com.jund.basis.core.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.jund.framework.jpa.base.entity.BaseEntity;
import org.springframework.data.rest.core.annotation.Description;

@Description("{title: '用户应用关联类'}")
@Entity
@Table(name = "plt_sec_role_app")
public class RoleApp extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@Description("{title: '角色ID'}")
	@Column(name = "role_id", nullable = false, length = 40)
	private Long roleId;

	@Description("{title: '应用ID'}")
	@Column(name = "app_id", nullable = false, length = 40)
	private Long appId;

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public Long getAppId() {
		return this.appId;
	}

	public void setAppId(Long appId) {
		this.appId = appId;
	}

	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		RoleApp pojo = (RoleApp) o;

		if (roleId != null ? !roleId.equals(pojo.roleId) : pojo.roleId != null)
			return false;
		if (appId != null ? !appId.equals(pojo.appId) : pojo.appId != null)
			return false;

		return true;
	}

	public int hashCode() {
		int result = 0;
		result = (roleId != null ? roleId.hashCode() : 0);
		result = 31 * result + (appId != null ? appId.hashCode() : 0);

		return result;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder(getClass().getSimpleName());

		sb.append(" [");
		sb.append("主键").append("：'").append(getId()).append("', ");
		sb.append("用户主键").append("：'").append(getRoleId()).append("', ");
		sb.append("应用主键").append("：'").append(getAppId()).append("'");
		sb.append("]");

		return sb.toString();
	}

}
