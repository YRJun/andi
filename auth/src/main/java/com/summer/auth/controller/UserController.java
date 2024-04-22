package com.summer.auth.controller;

import com.summer.auth.service.UserService;
import com.summer.common.model.request.AndiBaseRequest;
import com.summer.common.model.request.UserRequest;
import com.summer.common.model.response.AndiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @author Renjun Yu
 * @description
 * @date 2024/01/20 14:12
 */
@RestController
@RequestMapping("user")
@Tag(name = "UserController", description = "用户管理")
public class UserController {

    @Resource
    private UserService userService;

    @Operation(summary = "queryUser", description = "查询用户")
    @RequestMapping(value = "queryUser", method = RequestMethod.POST)
    public AndiResponse<?> queryUser(@RequestBody UserRequest request) {
        return userService.queryUser(request);
    }

    @Operation(summary = "esTest", description = "es查询测试")
    @RequestMapping(value = "esTest", method = RequestMethod.POST)
    public AndiResponse<?> esTest() {
        try {
            return userService.esTest();
        } catch (IOException e) {
            return AndiResponse.fail(e.getMessage());
        }
    }
}
