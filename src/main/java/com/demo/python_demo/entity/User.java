package com.demo.python_demo.entity;

import java.util.Date;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.io.Serializable;

/**
 * 用户信息实体类
 */
public class User implements Serializable {
    @JsonProperty("userId")
    private Integer userId;
    private String phone;
    private String account;
    private String password;
    private String nickname;
    private String avatar;
    private Integer groupType;
    private String intestTypes;
    private Date createTime;
    private Date updateTime;
    private String email;

    // Getter and Setter methods
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Integer getGroupType() {
        return groupType;
    }

    public void setGroupType(Integer groupType) {
        this.groupType = groupType;
    }

    public String getIntestTypes() {
        return intestTypes;
    }

    public void setIntestTypes(String intestTypes) {
        this.intestTypes = intestTypes;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Integer> getIntestTypesList() {
        if (this.intestTypes == null || this.intestTypes.isEmpty()) return new ArrayList<>();
        return Arrays.stream(this.intestTypes.split(","))
            .map(Integer::parseInt)
            .collect(Collectors.toList());
    }

    public void setIntestTypesList(List<Integer> intestTypesList) {
        this.intestTypes = String.join(",", intestTypesList.stream().map(String::valueOf).collect(Collectors.toList()));
    }
}
