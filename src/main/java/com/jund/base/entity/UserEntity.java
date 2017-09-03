package com.jund.base.entity;

import java.io.Serializable;

/**
 * Created by zhijund on 2017/9/2.
 */
public class UserEntity {

    private static final long serialVersionUID = 1L;
    private String id;
    private String account;
    private String nickname;
    private String password;
    private String accountType;
    private String trueName;
    private String sex;
    private String birthday;
    private String province;
    private String city;
    private String address;
    private String amount;
    private String tel;
    private String email;
    private String emailIsActive;
    private String freeze;
    private String lastLoginTime;
    private String lastLoginIp;
    private String lastLoginArea;
    private String diffAreaLogin;
    private String regeistDate;
    private String freezeStartdate;
    private String freezeEnddate;
    private int addressID;
    private String openId;
    private String accessToken;
    /**
     * 支付宝快捷登陆的用户ID
     */
    private String alipayUseId;
    /**
     * 新浪微博用户登陆返回的ID
     */
    private String sinaWeiboID;
    /**
     * 会员等级
     */
    private String rank;
    /**
     * 会员积分
     */
    private int score;

    /**
     * 根据会员积分获取会员等级
     *
     * @return
     */
    public String getRankCode() {
        if (this.score < 500) {
            return "R1";
        } else if (this.score >= 999) {
            return "R2";
        } else if (this.score >= 1000 && this.score <= 1999) {
            return "R3";
        } else if (this.score >= 2000 && this.score <= 4000) {
            return "R4";
        } else {
            return "R5";
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFreeze() {
        return freeze;
    }

    public void setFreeze(String freeze) {
        this.freeze = freeze;
    }


    public String getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(String lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getLastLoginIp() {
        return lastLoginIp;
    }

    public void setLastLoginIp(String lastLoginIp) {
        this.lastLoginIp = lastLoginIp;
    }

    public String getRegeistDate() {
        return regeistDate;
    }

    public void setRegeistDate(String regeistDate) {
        this.regeistDate = regeistDate;
    }

    public int getAddressID() {
        return addressID;
    }

    public void setAddressID(int addressID) {
        this.addressID = addressID;
    }

    public String getFreezeStartdate() {
        return freezeStartdate;
    }

    public void setFreezeStartdate(String freezeStartdate) {
        this.freezeStartdate = freezeStartdate;
    }

    public String getFreezeEnddate() {
        return freezeEnddate;
    }

    public void setFreezeEnddate(String freezeEnddate) {
        this.freezeEnddate = freezeEnddate;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAlipayUseId() {
        return alipayUseId;
    }

    public void setAlipayUseId(String alipayUseId) {
        this.alipayUseId = alipayUseId;
    }

    public String getSinaWeiboID() {
        return sinaWeiboID;
    }

    public void setSinaWeiboID(String sinaWeiboID) {
        this.sinaWeiboID = sinaWeiboID;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getTrueName() {
        return trueName;
    }

    public void setTrueName(String trueName) {
        this.trueName = trueName;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getEmailIsActive() {
        return emailIsActive;
    }

    public void setEmailIsActive(String emailIsActive) {
        this.emailIsActive = emailIsActive;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getLastLoginArea() {
        return lastLoginArea;
    }

    public void setLastLoginArea(String lastLoginArea) {
        this.lastLoginArea = lastLoginArea;
    }

    public String getDiffAreaLogin() {
        return diffAreaLogin;
    }

    public void setDiffAreaLogin(String diffAreaLogin) {
        this.diffAreaLogin = diffAreaLogin;
    }

}
