package com.jund.basis.core.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import com.jund.framework.jpa.base.entity.BaseEntity;
import org.springframework.data.rest.core.annotation.Description;


@Description("{title: '用户实体类'}")
@Entity
@Table(name = "plt_sec_user")
public class User extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Description("{title: '用户登录信息', type: 'oneToOne'}")
    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "ulogin_id", unique = true)
    private UserLogin userLogin;

    @Description("{title: '用户账号'}")
    @Column(name = "user_name", nullable = false, length = 40)
    private String userName;

    @Description("{title: '用户姓名'}")
    @Column(name = "real_name", nullable = false, length = 40)
    private String realName;

    @Description("{title: '所属机构', type: 'manyToOne'}")
    @ManyToOne
    @JoinColumn(name = "org_id", nullable = false)
    private Organ org;

    @Description("{title: '生效标识'}")
    @Column(name = "status", nullable = false, length = 1)
    private Integer status;

    @Description("{title: '身份证号'}")
    @Column(name = "idcard", length = 18)
    private String idcard;

    @Description("{title: '邮箱'}")
    @Column(name = "email", length = 64)
    private String email;

    @Description("{title: '备注'}")
    @Column(name = "remark", length = 85)
    private String remark;

    @Description("{title: '办公电话'}")
    @Column(name = "office_phone", length = 20)
    private String officePhone;

    @Description("{title: '移动电话'}")
    @Column(name = "mobile_phone", length = 20)
    private String mobilePhone;

    @Description("{title: '员工编号'}")
    @Column(name = "emp_no", length = 40)
    private String empNo;

    @ManyToMany
    @JoinTable(name = "plt_sec_user_role", joinColumns = {
            @JoinColumn(name = "user_id", referencedColumnName = "id")}, inverseJoinColumns = {
            @JoinColumn(name = "role_id", referencedColumnName = "id")})
    private Set<Role> roles = new HashSet<Role>();

    @Description("{title: '是否授权角色'}")
    @Column(name = "priv_flag", nullable = false, length = 1)
    private Integer privFlag;

    public void setId(Long id) {
        this.id = id;
    }

    public UserLogin getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(UserLogin userLogin) {
        this.userLogin = userLogin;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRealName() {
        return this.realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public Organ getOrg() {
        return org;
    }

    public void setOrg(Organ org) {
        this.org = org;
    }

    public Integer getStatus() {
        return this.status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getIdcard() {
        return this.idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getOfficePhone() {
        return this.officePhone;
    }

    public void setOfficePhone(String officePhone) {
        this.officePhone = officePhone;
    }

    public String getMobilePhone() {
        return this.mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getEmpNo() {
        return this.empNo;
    }

    public void setEmpNo(String empNo) {
        this.empNo = empNo;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public Integer getPrivFlag() {
        return privFlag;
    }

    public void setPrivFlag(Integer privFlag) {
        this.privFlag = privFlag;
    }

    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        User pojo = (User) o;

        if (id != null ? !id.equals(pojo.id) : pojo.id != null)
            return false;
        if (userName != null ? !userName.equals(pojo.userName) : pojo.userName != null)
            return false;

        return true;
    }

    public int hashCode() {
        int result = 0;
        result = (userName != null ? userName.hashCode() : 0);

        return result;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(getClass().getSimpleName());

        sb.append(" [");
        sb.append("用户主键").append("：'").append(getId()).append("', ");
        sb.append("用户名").append("：'").append(getUserName()).append("', ");
        sb.append("姓名").append("：'").append(getRealName()).append("' ");
        sb.append("]");

        return sb.toString();
    }

}
