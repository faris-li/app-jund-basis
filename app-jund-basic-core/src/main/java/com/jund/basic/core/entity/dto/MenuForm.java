package com.jund.basic.core.entity.dto;

import java.io.Serializable;

public class MenuForm implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long currId;
	private Long toId;
	private Integer type;

	
	public Long getCurrId() {
		return currId;
	}

	public void setCurrId(Long currId) {
		this.currId = currId;
	}

	public Long getToId() {
		return toId;
	}

	public void setToId(Long toId) {
		this.toId = toId;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

}
