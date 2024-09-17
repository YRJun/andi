package com.summer.common.model.andi;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.summer.common.util.JacksonUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * 用户表
 * @author Renjun Yu
 * @date 2024/01/06 12:40
 */
@Schema(description = "用户实体类")
public class AndiUserDO implements Serializable {
    /**
     * 用户ID
     */
    @Schema(description = "用户ID")
    private String userId;
    /**
     * 用户名
     */
    @Schema(description = "用户名")
    @NotBlank(message = "用户名不能为空")
    private String username;
    /**
     * 用户密码(加密)
     */
    @Schema(description = "用户密码")
    private String password;
    /**
     * 用户电子邮件地址
     */
    @Schema(description = "用户电子邮件地址")
    @Pattern(regexp = "\\w[-\\w.+]*@([A-Za-z0-9][-A-Za-z0-9]+\\.)+[A-Za-z]{2,14}", message = "邮箱格式错误")
    private String email;
    /**
     * 用户全名
     */
    @Schema(description = "用户全名")
    private String fullName;
    /**
     * 用户别名
     */
    @Schema(description = "用户别名")
    private String aliasName;
    /**
     * 用户生日
     */
    @Schema(description = "用户生日")
    private LocalDate birthdate;
    /**
     * 用户性别:Male、Female、Other
     */
    @Schema(description = "用户性别")
    @Pattern(regexp = "Male|Female|Other", message = "性别仅支持:Male/Female/Other")
    private String gender;
    /**
     * 用户电话号码
     */
    @Schema(description = "用户电话号码")
    private String phone;
    /**
     * 用户地址
     */
    @Schema(description = "用户地址")
    private String address;
    /**
     * 用户所在国家
     */
    @Schema(description = "用户所在国家")
    private String country;
    /**
     * 用户账号是否启用，1：已启用，0：未启用
     */
    @Schema(description = "用户账号是否启用")
    private Boolean enable;
    /**
     * 用户账户是否过期，1：已过期，0：未过期
     */
    @Schema(description = "用户账户是否过期")
    private Boolean accountExpire;
    /**
     * 用户密码是否过期，1：已过期，0：未过期
     */
    @Schema(description = "用户密码是否过期")
    private Boolean passwordExpire;
    /**
     * 用户账户是否被锁定，1：已锁定，0：未锁定
     */
    @Schema(description = "用户账户是否被锁定")
    private Boolean lock;
    /**
     * 用户最近登录时间
     */
    @Schema(description = "用户最近登录时间")
    private LocalDateTime lastLoginTime;
    /**
     * 用户头像的URL或文件路径
     */
    @Schema(description = "用户头像的URL或文件路径")
    private String profilePicture;
    /**
     * 用户创建时间
     */
    @Schema(description = "用户创建时间")
    @JsonSerialize(using = JacksonUtils.LocalDateTimeIso8601Serializer.class)
    private LocalDateTime createTime;
    /**
     * 用户信息最近更新时间
     */
    @Schema(description = "用户信息最近更新时间")
    private LocalDateTime updateTime;

    public String getUserId() {
        return userId;
    }

    public void setUserId(final String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(final String fullName) {
        this.fullName = fullName;
    }

    public String getAliasName() {
        return aliasName;
    }

    public void setAliasName(final String aliasName) {
        this.aliasName = aliasName;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(final LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(final String gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(final String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(final String address) {
        this.address = address;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(final String country) {
        this.country = country;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(final Boolean enable) {
        this.enable = enable;
    }

    public Boolean getAccountExpire() {
        return accountExpire;
    }

    public void setAccountExpire(final Boolean accountExpire) {
        this.accountExpire = accountExpire;
    }

    public Boolean getPasswordExpire() {
        return passwordExpire;
    }

    public void setPasswordExpire(final Boolean passwordExpire) {
        this.passwordExpire = passwordExpire;
    }

    public Boolean getLock() {
        return lock;
    }

    public void setLock(final Boolean lock) {
        this.lock = lock;
    }

    public LocalDateTime getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(final LocalDateTime lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(final String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(final LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(final LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }
}
