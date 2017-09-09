package com.jund.basic.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.jund.framework.jpa.base.entity.BaseEntity;
import org.springframework.data.rest.core.annotation.Description;

@Description("{title: '用户角色关联类'}")
@Entity
@Table(name = "plt_sec_user_role")
public class UserRole extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@Description("{title: '用户ID'}")
	@Column(name = "user_id", nullable = false, length = 40)
	private Long userId;

	@Description("{title: '角色ID'}")
	@Column(name = "role_id", nullable = false, length = 40)
	private Long roleId;

	public Long getUserId() {
		return this.userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getRoleId() {
		return this.roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		UserRole pojo = (UserRole) o;

		if (userId != null ? !userId.equals(pojo.userId) : pojo.userId != null)
			return false;
		if (roleId != null ? !roleId.equals(pojo.roleId) : pojo.roleId != null)
			return false;

		return true;
	}

	public int hashCode() {
		int result = 0;
		result = (userId != null ? userId.hashCode() : 0);
		result = 31 * result + (roleId != null ? roleId.hashCode() : 0);

		return result;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder(getClass().getSimpleName());

		sb.append(" [");
		sb.append("主键").append("：'").append(getId()).append("', ");
		sb.append("用户主键").append("：'").append(getUserId()).append("', ");
		sb.append("角色主键").append("：'").append(getRoleId()).append("'");
		sb.append("]");

		return sb.toString();
	}

}
