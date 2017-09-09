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
@Table(name = "plt_sec_app")
@Description("{title: '应用实体类'}")
public class App extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@Description("{title: '应用编号'}")
	@Column(name = "app_code", nullable = false, length = 40)
	private String appCode;

	@Description("{title: '应用名称'}")
	@Column(name = "app_name", nullable = false, length = 40)
	private String appName;

	@Description("{title: '备注'}")
	@Column(name = "remark", length = 1000)
	private String remark;

	@Description("{title: '应用地址'}")
	@Column(name = "app_addr", nullable = false, length = 255)
	private String appAddr;

	@Description("{title: '菜单图标'}")
	@Column(name = "app_icon", length = 255)
	private String appIcon;
	
	@Description("{title: '生效标识'}")
	@Column(name = "status", nullable = false, length = 1)
	private Integer status;

	public void setId(Long id){
		this.id = id;
	}
	
	public String getAppCode() {
		return appCode;
	}

	public void setAppCode(String appCode) {
		this.appCode = appCode;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getAppAddr() {
		return appAddr;
	}

	public void setAppAddr(String appAddr) {
		this.appAddr = appAddr;
	}

	public String getAppIcon() {
		return appIcon;
	}

	public void setAppIcon(String appIcon) {
		this.appIcon = appIcon;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		App pojo = (App) o;

		if (id != null ? !id.equals(pojo.id) : pojo.id != null)
			return false;
		if (appCode != null ? !appCode.equals(pojo.appCode) : pojo.appCode != null)
			return false;

		return true;
	}

	public int hashCode() {
		int result = 0;
		result = (appCode != null ? appCode.hashCode() : 0);

		return result;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder(getClass().getSimpleName());

		sb.append(" [");
		sb.append("主键").append("：'").append(getId()).append("', ");
		sb.append("应用编码").append("：'").append(getAppCode()).append("', ");
		sb.append("应用名称").append("：'").append(getAppName()).append("', ");
		sb.append("应用地址").append("：'").append(getAppAddr()).append("', ");
		sb.append("]");

		return sb.toString();
		
	}

}
