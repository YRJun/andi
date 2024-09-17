package com.summer.common.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * @author Renjun Yu
 * @date 2024/07/21 10:25
 */
public class CreateUserVO implements Serializable {
    /**
     * 用户名
     */
    @Schema(description = "用户名")
    @NotBlank(message = "用户名不能为空")
    private String username;
    /**
     * 用户密码(前端加密)
     */
    @Schema(description = "用户密码")
    private String password;
    /**
     * 用户电子邮件地址
     */
    @Schema(description = "用户电子邮件地址")
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
     * 用户头像的URL或文件路径
     */
    @Schema(description = "用户头像的URL或文件路径")
    private String profilePicture;

    public @NotBlank(message = "用户名不能为空") String getUsername() {
        return username;
    }

    public void setUsername(final @NotBlank(message = "用户名不能为空") String username) {
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

    public @Pattern(regexp = "Male|Female|Other", message = "性别仅支持:Male/Female/Other") String getGender() {
        return gender;
    }

    public void setGender(final @Pattern(regexp = "Male|Female|Other", message = "性别仅支持:Male/Female/Other") String gender) {
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

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(final String profilePicture) {
        this.profilePicture = profilePicture;
    }
}
