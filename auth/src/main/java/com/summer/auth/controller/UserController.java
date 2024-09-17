package com.summer.auth.controller;

import com.summer.auth.service.UserService;
import com.summer.common.config.UseExceptionHandler;
import com.summer.common.model.response.AndiResponse;
import com.summer.common.model.vo.CreateUserVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @author Renjun Yu
 * @description
 * @date 2024/01/20 14:12
 */
@RestController
@RequestMapping("user-management")
@RequiredArgsConstructor
@Tag(name = "UserController", description = "用户管理")
@UseExceptionHandler
public class UserController {

    private final UserService userService;

    @Operation(summary = "查询用户", description = "查询用户")
    @RequestMapping(value = "users/{username}", method = RequestMethod.GET)
    public AndiResponse<?> queryUser(@PathVariable String username) {
        return userService.getUser(username);
    }

    @Operation(summary = "创建用户", description = "创建用户")
    @RequestMapping(value = "users", method = RequestMethod.POST)
    public AndiResponse<?> createUser(@RequestBody CreateUserVO userInfo) {
        return userService.insertUser(userInfo);
    }
}
