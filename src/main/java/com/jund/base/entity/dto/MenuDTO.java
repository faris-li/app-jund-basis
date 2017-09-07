package com.jund.base.entity.dto;

import java.io.Serializable;

public class MenuDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String menuName;
	private String menuUrl;
	private Long pid;
	private Integer expandFlag;
	private String menuIcon;
	private Integer sortNo;

	public MenuDTO(Long id, String menuName, String menuUrl, Long pid, Integer expandFlag, String menuIcon,
			Integer sortNo) {
		this.id = id;
		this.expandFlag = expandFlag;
		this.menuIcon = menuIcon;
		this.menuName = menuName;
		this.menuUrl = menuUrl;
		this.pid = pid;
		this.sortNo = sortNo;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public String getMenuUrl() {
		return menuUrl;
	}

	public void setMenuUrl(String menuUrl) {
		this.menuUrl = menuUrl;
	}

	public Integer getExpandFlag() {
		return expandFlag;
	}

	public void setExpandFlag(Integer expandFlag) {
		this.expandFlag = expandFlag;
	}

	public Long getPid() {
		return pid;
	}

	public void setPid(Long pid) {
		this.pid = pid;
	}

	public String getMenuIcon() {
		return menuIcon;
	}

	public void setMenuIcon(String menuIcon) {
		this.menuIcon = menuIcon;
	}

	public Integer getSortNo() {
		return sortNo;
	}

	public void setSortNo(Integer sortNo) {
		this.sortNo = sortNo;
	}

}
