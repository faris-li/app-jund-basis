package com.jund.basic.core.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.jund.framework.jpa.base.entity.BaseEntity;
import org.springframework.data.rest.core.annotation.Description;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 *
 */
@Description("{title: '用户登陆信息类'}")
@Entity
@Table(name = "plt_sec_user_login")
@JsonIgnoreProperties("user")
public class UserLogin extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Description("{title: '用户名'}")
    @OneToOne(mappedBy = "userLogin", fetch = FetchType.EAGER)
    private User user;

    @Description("{title: '密码'}")
    @Column(name = "password", nullable = false, length = 128)
    private String password;

    @Description("{title: '启用&锁定状态'}")
    @Column(name = "user_flag", nullable = false, length = 1)
    private Integer userFlag;

    @Description("{title: '用户锁定时间'}")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @Column(name = "locked_time", length = 26)
    private Date lockedTime;

    @Description("{title: '最后登录时间'}")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @Column(name = "last_login_time", length = 26)
    private Date lastLoginTime;

    @Description("{title: '登录次数'}")
    @Column(name = "login_num")
    private Long loginNum;

    @Description("{title: '是否首次登录'}")
    @Column(name = "is_first_login", length = 1)
    private Integer isFirstLogin;

    @Description("{title: '密码修改时间'}")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @Column(name = "pwd_modify_time", length = 26)
    private Date pwdModifyTime;

    @Description("{title: '密码错误次数'}")
    @Column(name = "pwd_error_num")
    private Integer pwdErrorNum;

    @Description("{title: '上次密码'}")
    @Column(name = "old_pwd", length = 128)
    private String oldPwd;

    @Description("{title: '历史密码'}")
    @Column(name = "his_pwd", length = 4000)
    private String hisPwd;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getUserFlag() {
        return this.userFlag;
    }

    public void setUserFlag(Integer userFlag) {
        this.userFlag = userFlag;
    }

    public Date getLockedTime() {
        return this.lockedTime;
    }

    public void setLockedTime(Date lockedTime) {
        this.lockedTime = lockedTime;
    }

    public Date getLastLoginTime() {
        return this.lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public Long getLoginNum() {
        return this.loginNum;
    }

    public void setLoginNum(Long loginNum) {
        this.loginNum = loginNum;
    }

    public Integer getIsFirstLogin() {
        return this.isFirstLogin;
    }

    public void setIsFirstLogin(Integer isFirstLogin) {
        this.isFirstLogin = isFirstLogin;
    }

    public Date getPwdModifyTime() {
        return this.pwdModifyTime;
    }

    public void setPwdModifyTime(Date pwdModifyTime) {
        this.pwdModifyTime = pwdModifyTime;
    }

    public Integer getPwdErrorNum() {
        return this.pwdErrorNum;
    }

    public void setPwdErrorNum(Integer pwdErrorNum) {
        this.pwdErrorNum = pwdErrorNum;
    }

    public String getOldPwd() {
        return this.oldPwd;
    }

    public void setOldPwd(String oldPwd) {
        this.oldPwd = oldPwd;
    }

    public String getHisPwd() {
        return this.hisPwd;
    }

    public void setHisPwd(String hisPwd) {
        this.hisPwd = hisPwd;
    }

    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        UserLogin pojo = (UserLogin) o;

        if (id != null ? !id.equals(pojo.id) : pojo.id != null)
            return false;
        if (user != null ? !user.equals(pojo.user) : pojo.user != null)
            return false;

        return true;
    }

    public int hashCode() {
        int result = 0;
        result = (user != null ? user.hashCode() : 0);
        return result;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(getClass().getSimpleName());

        sb.append(" [");
        sb.append("用户主键").append("：'").append(getId()).append("', ");
        sb.append("用户名").append("：'").append(getUser().getUserName()).append("', ");
        sb.append("启用&锁定状态").append("：'").append(getUserFlag()).append("' ");
        sb.append("]");

        return sb.toString();
    }

}
