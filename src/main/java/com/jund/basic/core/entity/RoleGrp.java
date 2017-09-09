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
@Table(name = "plt_sec_role_grp")
@Description("{title: '角色分级'}")
public class RoleGrp extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@Description("{title: '父角色id'}")
	@Column(name = "sup_role_id", nullable = false)
	private Long supRoleId;

	@Description("{title: '子角色id'}")
	@Column(name = "sub_role_id", nullable = false)
	private Long subRoleId;

	public Long getSupRoleId() {
		return this.supRoleId;
	}

	public void setSupRoleId(Long supRoleId) {
		this.supRoleId = supRoleId;
	}

	public Long getSubRoleId() {
		return this.subRoleId;
	}

	public void setSubRoleId(Long subRoleId) {
		this.subRoleId = subRoleId;
	}

	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		RoleGrp pojo = (RoleGrp) o;

		if (supRoleId != null ? !supRoleId.equals(pojo.supRoleId) : pojo.supRoleId != null)
			return false;
		if (subRoleId != null ? !subRoleId.equals(pojo.subRoleId) : pojo.subRoleId != null)
			return false;

		return true;
	}

	public int hashCode() {
		int result = 0;
		result = (supRoleId != null ? supRoleId.hashCode() : 0);
		result = 31 * result + (subRoleId != null ? subRoleId.hashCode() : 0);

		return result;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder(getClass().getSimpleName());

		sb.append(" [");
		sb.append("主键").append("：'").append(getId()).append("', ");
		sb.append("父角色").append("：'").append(getSupRoleId()).append("', ");
		sb.append("子角色").append("：'").append(getSubRoleId()).append("'");
		sb.append("]");

		return sb.toString();
	}

}
