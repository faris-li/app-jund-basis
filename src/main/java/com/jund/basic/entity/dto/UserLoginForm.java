package com.jund.basic.entity.dto;

import java.io.Serializable;

public class UserLoginForm implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long[] userIds;
	
	private Long userId;
	
	private String password;

	public Long[] getUserIds() {
		return userIds;
	}

	public void setUserIds(Long[] userIds) {
		this.userIds = userIds;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	
}
