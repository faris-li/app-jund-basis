package com.jund.basis.core.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.jund.framework.jpa.base.entity.BaseEntity;
import org.springframework.data.rest.core.annotation.Description;

/**
 * @author tanghui
 */
@Entity
@Table(name = "plt_sec_role_menu")
@Description("{title: '角色菜单关系类'}")
public class RoleMenu extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@Description("{title: '角色id'}")
	@Column(name = "role_id", nullable = false)
	private Long roleId;

	@Description("{title: '菜单id'}")
	@Column(name = "menu_id", nullable = false)
	private Long menuId;

	public Long getRoleId() {
		return this.roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public Long getMenuId() {
		return menuId;
	}

	public void setMenuId(Long menuId) {
		this.menuId = menuId;
	}

	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		RoleMenu pojo = (RoleMenu) o;

		if (roleId != null ? !roleId.equals(pojo.roleId) : pojo.roleId != null)
			return false;
		if (menuId != null ? !menuId.equals(pojo.menuId) : pojo.menuId != null)
			return false;

		return true;
	}

	public int hashCode() {
		int result = 0;
		result = (roleId != null ? roleId.hashCode() : 0);
		result = 31 * result + (menuId != null ? menuId.hashCode() : 0);

		return result;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder(getClass().getSimpleName());

		sb.append(" [");
		sb.append("主键").append("：'").append(getId()).append("', ");
		sb.append("角色id").append("：'").append(getRoleId()).append("', ");
		sb.append("菜单id").append("：'").append(getMenuId()).append("'");
		sb.append("]");

		return sb.toString();
	}

}
