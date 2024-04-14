package com.summer.common.model.andi;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
/**
 * @author Renjun Yu
 * @description
 * @date 2024/01/06 12:40
 */
@Data
@Schema(name = "用户实体")
public class AndiUser {
    private int id;
    private String username;
    private String password;
    private String email;
    private String fullName;
    private String aliasName;
    private LocalDate birthdate;
    private String gender;
    private String phone;
    private String address;
    private String country;
    private int isActive;
    private LocalDateTime lastLoginTime;
    private String profilePicture;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}

