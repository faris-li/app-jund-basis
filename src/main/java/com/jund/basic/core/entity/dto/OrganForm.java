package com.jund.basic.core.entity.dto;

import java.io.Serializable;

import com.jund.platformwork.security.model.Organ;

public class OrganForm implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;

	private String orgCode;

	private String orgName;

	private Integer orgLevel;

	private Organ parentOrgan;

	private Long oldParentId;

	private String orgSeq;

	private Integer status;

	private String remark;

	private Long[] ids;

	
	public Organ getParentOrgan() {
		return parentOrgan;
	}

	public void setParentOrgan(Organ parentOrgan) {
		this.parentOrgan = parentOrgan;
	}

	public Long getOldParentId() {
		return oldParentId;
	}

	public void setOldParentId(Long oldParentId) {
		this.oldParentId = oldParentId;
	}

	public Long[] getIds() {
		return ids;
	}

	public void setIds(Long[] ids) {
		this.ids = ids;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public Integer getOrgLevel() {
		return orgLevel;
	}

	public void setOrgLevel(Integer orgLevel) {
		this.orgLevel = orgLevel;
	}

	public String getOrgSeq() {
		return orgSeq;
	}

	public void setOrgSeq(String orgSeq) {
		this.orgSeq = orgSeq;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
