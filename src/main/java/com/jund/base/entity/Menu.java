package com.jund.base.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import com.jund.framework.jpa.base.entity.BaseEntity;
import org.springframework.data.rest.core.annotation.Description;


/**
 *
 */
@Description("{title: '菜单实体类'}")
@Entity
@Table(name = "plt_sec_menu")
public class Menu extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@Description("{title: '菜单名'}")
	@Column(name = "menu_name", nullable = false, length = 40)
	private String menuName;

	@Description("{title: '菜单号'}")
	@Column(name = "menu_code", nullable = false, length = 40)
	private String menuCode;

	@Description("{title: '菜单路径'}")
	@Column(name = "menu_url", length = 255)
	private String menuUrl;

	@Description("{title: '菜单图标'}")
	@Column(name = "menu_icon", length = 255)
	private String menuIcon;

	@Description("{title: '所属应用', type: 'manyToOne'}")
	@ManyToOne
	@JoinColumn(name = "app_id", nullable = false)
	private App app;

	@Description("{title: '父菜单'}")
	@ManyToOne
	@JoinColumn(name = "pid")
	private Menu parentMenu;

	@Description("{title: '显示顺序'}")
	@Column(name = "sort_no", length = 2)
	private Integer sortNo;

	@Description("{title: '菜单类型'}")
	@Column(name = "menu_type", length = 1)
	private Integer menuType;

	@Description("{title: '备注'}")
	@Column(name = "remark", length = 85)
	private String remark;

	@Description("{title: '是否展开'}")
	@Column(name = "expand_flag", nullable = false, length = 1)
	private Integer expandFlag;

	@Description("{title: '生效标识'}")
	@Column(name = "status", nullable = false, length = 1)
	private Integer status;

	public void setId(Long id) {
		this.id = id;
	}

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public String getMenuCode() {
		return menuCode;
	}

	public void setMenuCode(String menuCode) {
		this.menuCode = menuCode;
	}

	public String getMenuUrl() {
		return menuUrl;
	}

	public void setMenuUrl(String menuUrl) {
		this.menuUrl = menuUrl;
	}

	public String getMenuIcon() {
		return menuIcon;
	}

	public void setMenuIcon(String menuIcon) {
		this.menuIcon = menuIcon;
	}

	public App getApp() {
		return app;
	}

	public void setApp(App app) {
		this.app = app;
	}

	public Menu getParentMenu() {
		return parentMenu;
	}

	public void setParentMenu(Menu parentMenu) {
		this.parentMenu = parentMenu;
	}

	public Integer getSortNo() {
		return sortNo;
	}

	public void setSortNo(Integer sortNo) {
		this.sortNo = sortNo;
	}

	public Integer getMenuType() {
		return menuType;
	}

	public void setMenuType(Integer menuType) {
		this.menuType = menuType;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getExpandFlag() {
		return expandFlag;
	}

	public void setExpandFlag(Integer expandFlag) {
		this.expandFlag = expandFlag;
	}

	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		Menu pojo = (Menu) o;

		if (menuCode != null ? !menuCode.equals(pojo.menuCode) : pojo.menuCode != null)
			return false;
		if (menuName != null ? !menuName.equals(pojo.menuName) : pojo.menuName != null)
			return false;

		return true;
	}

	public int hashCode() {
		int result = 0;
		result = (menuCode != null ? menuCode.hashCode() : 0);
		return result;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder(getClass().getSimpleName());

		sb.append(" [");
		sb.append("菜单主键").append("：'").append(getId()).append("', ");
		sb.append("菜单号").append("：'").append(getMenuCode()).append("', ");
		sb.append("菜单名称").append("：'").append(getMenuName()).append("', ");
		sb.append("]");

		return sb.toString();
	}

}
