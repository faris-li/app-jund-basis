package com.jund.basic.entity.dto;

import java.io.Serializable;

import com.jund.platformwork.security.model.Organ;

public class UserForm implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	private String userName;
	
	private String realName;
	
	private Organ organ;
	
	private Long[] roleIds;
	
	private Integer status;
	
	private String idcard;
	
	private String email;
	
	private String remark;
	
	private String mobilePhone;
	
	private String empNo;
	
	private Integer privFlag;
	
	private Integer userFlag;
	
	
	public Integer getUserFlag() {
		return userFlag;
	}

	public void setUserFlag(Integer userFlag) {
		this.userFlag = userFlag;
	}

	public Organ getOrgan() {
		return organ;
	}

	public void setOrgan(Organ organ) {
		this.organ = organ;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public Long[] getRoleIds() {
		return roleIds;
	}

	public void setRoleIds(Long[] roleIds) {
		this.roleIds = roleIds;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getIdcard() {
		return idcard;
	}

	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public String getEmpNo() {
		return empNo;
	}

	public void setEmpNo(String empNo) {
		this.empNo = empNo;
	}

	public Integer getPrivFlag() {
		return privFlag;
	}

	public void setPrivFlag(Integer privFlag) {
		this.privFlag = privFlag;
	}

}
