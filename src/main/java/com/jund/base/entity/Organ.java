package com.jund.base.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import com.jund.framework.jpa.base.entity.BaseEntity;
import org.springframework.data.rest.core.annotation.Description;

@Description("{title: '机构实体类'}")
@Entity
@Table(name = "plt_sec_organ")
public class Organ extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@Description("{title: '机构号'}")
	@Column(name = "org_code", nullable = false, length = 40)
	private String orgCode;

	@Description("{title: '机构名'}")
	@Column(name = "org_name", nullable = false, length = 40)
	private String orgName;

	@Description("{title: '机构层次'}")
	@Column(name = "org_level")
	private Integer orgLevel;

	@Description("{title: '上级机构'}")
	@ManyToOne
	@JoinColumn(name = "pid")
	private Organ parentOrgan;

	@Description("{title: '机构序列'}")
	@Column(name = "org_seq", nullable = false, length = 30)
	private String orgSeq;

	@Description("{title: '状态'}")
	@Column(name = "status", nullable = false, length = 1)
	private Integer status;

	@Description("{title: '备注'}")
	@Column(name = "remark", length = 85)
	private String remark;

	public void setId(Long id) {
		this.id = id;
	}

	public String getOrgCode() {
		return this.orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	public String getOrgName() {
		return this.orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public Integer getOrgLevel() {
		return this.orgLevel;
	}

	public void setOrgLevel(Integer orgLevel) {
		this.orgLevel = orgLevel;
	}

	public Organ getParentOrgan() {
		return parentOrgan;
	}

	public void setParentOrgan(Organ parentOrgan) {
		this.parentOrgan = parentOrgan;
	}

	public String getOrgSeq() {
		return this.orgSeq;
	}

	public void setOrgSeq(String orgSeq) {
		this.orgSeq = orgSeq;
	}

	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		Organ pojo = (Organ) o;

		if (id != null ? !id.equals(pojo.id) : pojo.id != null)
			return false;
		if (orgCode != null ? !orgCode.equals(pojo.orgCode) : pojo.orgCode != null)
			return false;

		return true;
	}

	public int hashCode() {
		int result = 0;
		result = (orgCode != null ? orgCode.hashCode() : 0);
		return result;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder(getClass().getSimpleName());

		sb.append(" [");
		sb.append("机构主键").append("：'").append(getId()).append("', ");
		sb.append("机构编码").append("：'").append(getOrgCode()).append("', ");
		sb.append("机构名称").append("：'").append(getOrgName()).append("' ");
		sb.append("]");

		return sb.toString();
	}

}
