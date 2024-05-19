package com.summer.auth.controller;

import com.summer.auth.service.UserService;
import com.summer.common.model.response.AndiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

/**
 * @author Renjun Yu
 * @description
 * @date 2024/01/20 14:12
 */
@RestController
@RequestMapping("user-management")
@Tag(name = "UserController", description = "用户管理")
public class UserController {

    @Resource
    private UserService userService;

    @Operation(summary = "queryUser", description = "查询用户")
    @RequestMapping(value = "users/{username}", method = RequestMethod.POST)
    public AndiResponse<?> queryUser(@PathVariable final String username) {
        return userService.queryUser(username);
    }
}
