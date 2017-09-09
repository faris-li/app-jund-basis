package com.jund.basic.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.jund.framework.jpa.base.entity.BaseEntity;
import org.springframework.data.rest.core.annotation.Description;


@Entity
@Description("{title: '密码配置规则实体类'}")
@Table(name = "plt_sec_pwd_rule")
public class PwdRule extends BaseEntity {

	private static final long serialVersionUID = -3879158062178162070L;

	@Description("{title: '密码长度最小值'}")
	@Column(name = "min_length", nullable = false)
	private Integer minLength;

	@Description("{title: '密码最大长度'}")
	@Column(name = "max_length", nullable = false)
	private Integer maxLength;

	@Description("{title: '是否包含大写字母'}")
	@Column(name = "contains_az_up", nullable = false, length = 1)
	private Integer containsAZ;

	@Description("{title: '是否包含小写字母'}")
	@Column(name = "contains_az_low", nullable = false, length = 1)
	private Integer containsaz;

	@Description("{title: '是否包含数字'}")
	@Column(name = "contains_09", nullable = false, length = 1)
	private Integer contains09;

	@Description("{title: '包含的特殊字符'}")
	@Column(name = "contains_special_chars", length = 128)
	private String containsSpecialChars;

	@Description("{title: '是否允许包含用户名'}")
	@Column(name = "contains_username", nullable = false, length = 1)
	private Integer containsUsername;

	@Description("{title: '历史密码不允许重复的次数'}")
	@Column(name = "not_repeat_his_numbs", nullable = false)
	private Integer notRepeatHisNumbs;

	@Description("{title: '密码错误次数'}")
	@Column(name = "error_numbs", nullable = false)
	private Integer errorNumbs;

	@Description("{title: '首次登陆是否修改密码'}")
	@Column(name = "reset_at_first_login", nullable = false, length = 1)
	private Integer resetAtFirstLogin;

	@Description("{title: '密码修改间隔天数'}")
	@Column(name = "last_time_in", nullable = false)
	private Integer lastTimeIn;

	@Description("{title: '默认密码'}")
	@Column(name = "default_pwd", nullable = false, length = 32)
	private String defaultPwd;

	public Integer getMinLength() {
		return minLength;
	}

	public void setMinLength(Integer minLength) {
		this.minLength = minLength;
	}

	public Integer getMaxLength() {
		return maxLength;
	}

	public void setMaxLength(Integer maxLength) {
		this.maxLength = maxLength;
	}

	public Integer getContainsAZ() {
		return containsAZ;
	}

	public void setContainsAZ(Integer containsAZ) {
		this.containsAZ = containsAZ;
	}

	public Integer getContainsaz() {
		return containsaz;
	}

	public void setContainsaz(Integer containsaz) {
		this.containsaz = containsaz;
	}

	public Integer getContains09() {
		return contains09;
	}

	public void setContains09(Integer contains09) {
		this.contains09 = contains09;
	}

	public String getContainsSpecialChars() {
		return containsSpecialChars;
	}

	public void setContainsSpecialChars(String containsSpecialChars) {
		this.containsSpecialChars = containsSpecialChars;
	}

	public Integer getContainsUsername() {
		return containsUsername;
	}

	public void setContainsUsername(Integer containsUsername) {
		this.containsUsername = containsUsername;
	}

	public Integer getNotRepeatHisNumbs() {
		return notRepeatHisNumbs;
	}

	public void setNotRepeatHisNumbs(Integer notRepeatHisNumbs) {
		this.notRepeatHisNumbs = notRepeatHisNumbs;
	}

	public Integer getErrorNumbs() {
		return errorNumbs;
	}

	public void setErrorNumbs(Integer errorNumbs) {
		this.errorNumbs = errorNumbs;
	}

	public Integer getResetAtFirstLogin() {
		return resetAtFirstLogin;
	}

	public void setResetAtFirstLogin(Integer resetAtFirstLogin) {
		this.resetAtFirstLogin = resetAtFirstLogin;
	}

	public Integer getLastTimeIn() {
		return lastTimeIn;
	}

	public void setLastTimeIn(Integer lastTimeIn) {
		this.lastTimeIn = lastTimeIn;
	}

	public String getDefaultPwd() {
		return defaultPwd;
	}

	public void setDefaultPwd(String defaultPwd) {
		this.defaultPwd = defaultPwd;
	}

	public String toString() {
		return "PasswordRules [密码最小长度：" + minLength + ", 密码最大长度：" + maxLength + ", 是否包含大写字母：" + containsAZ
				+ ", 是否包含小写字母：" + containsaz + ", 是否包含数字：" + contains09 + ", 是否包含特殊字符：" + containsSpecialChars
				+ ", 不允许包含用户名：" + containsUsername + ", 历史密码不允许重复的次数：" + notRepeatHisNumbs + ", 密码错误次数：" + errorNumbs
				+ ", 首次登陆是否修改密码：" + resetAtFirstLogin + ", 密码修改间隔时间：" + lastTimeIn + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((contains09 == null) ? 0 : contains09.hashCode());
		result = prime * result + ((containsAZ == null) ? 0 : containsAZ.hashCode());
		result = prime * result + ((containsSpecialChars == null) ? 0 : containsSpecialChars.hashCode());
		result = prime * result + ((containsaz == null) ? 0 : containsaz.hashCode());
		result = prime * result + ((defaultPwd == null) ? 0 : defaultPwd.hashCode());
		result = prime * result + ((errorNumbs == null) ? 0 : errorNumbs.hashCode());
		result = prime * result + ((lastTimeIn == null) ? 0 : lastTimeIn.hashCode());
		result = prime * result + ((maxLength == null) ? 0 : maxLength.hashCode());
		result = prime * result + ((minLength == null) ? 0 : minLength.hashCode());
		result = prime * result + ((containsUsername == null) ? 0 : containsUsername.hashCode());
		result = prime * result + ((notRepeatHisNumbs == null) ? 0 : notRepeatHisNumbs.hashCode());
		result = prime * result + ((resetAtFirstLogin == null) ? 0 : resetAtFirstLogin.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PwdRule other = (PwdRule) obj;
		if (contains09 == null) {
			if (other.contains09 != null)
				return false;
		} else if (!contains09.equals(other.contains09))
			return false;
		if (containsAZ == null) {
			if (other.containsAZ != null)
				return false;
		} else if (!containsAZ.equals(other.containsAZ))
			return false;
		if (containsSpecialChars == null) {
			if (other.containsSpecialChars != null)
				return false;
		} else if (!containsSpecialChars.equals(other.containsSpecialChars))
			return false;
		if (containsaz == null) {
			if (other.containsaz != null)
				return false;
		} else if (!containsaz.equals(other.containsaz))
			return false;
		if (defaultPwd == null) {
			if (other.defaultPwd != null)
				return false;
		} else if (!defaultPwd.equals(other.defaultPwd))
			return false;
		if (errorNumbs == null) {
			if (other.errorNumbs != null)
				return false;
		} else if (!errorNumbs.equals(other.errorNumbs))
			return false;
		if (lastTimeIn == null) {
			if (other.lastTimeIn != null)
				return false;
		} else if (!lastTimeIn.equals(other.lastTimeIn))
			return false;
		if (maxLength == null) {
			if (other.maxLength != null)
				return false;
		} else if (!maxLength.equals(other.maxLength))
			return false;
		if (minLength == null) {
			if (other.minLength != null)
				return false;
		} else if (!minLength.equals(other.minLength))
			return false;
		if (containsUsername == null) {
			if (other.containsUsername != null)
				return false;
		} else if (!containsUsername.equals(other.containsUsername))
			return false;
		if (notRepeatHisNumbs == null) {
			if (other.notRepeatHisNumbs != null)
				return false;
		} else if (!notRepeatHisNumbs.equals(other.notRepeatHisNumbs))
			return false;
		if (resetAtFirstLogin == null) {
			if (other.resetAtFirstLogin != null)
				return false;
		} else if (!resetAtFirstLogin.equals(other.resetAtFirstLogin))
			return false;
		return true;
	}

}
