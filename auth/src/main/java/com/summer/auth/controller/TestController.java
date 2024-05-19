package com.summer.auth.controller;

import com.summer.auth.service.TestService;
import com.summer.auth.service.UserService;
import com.summer.common.model.response.AndiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * @author Renjun Yu
 * @description
 * @date 2024/01/20 14:12
 */
@RestController
@RequestMapping("user-management")
@Tag(name = "UserController", description = "用户管理")
public class TestController {

    @Resource
    private TestService testService;

    @Operation(summary = "esTest", description = "es查询测试")
    @RequestMapping(value = "esTest", method = RequestMethod.POST)
    public AndiResponse<?> esTest() {
        try {
            return testService.esTest1();
        } catch (IOException e) {
            return AndiResponse.fail(e.getMessage());
        }
    }

    @Operation(summary = "snowflakeIdTest", description = "雪花算法测试")
    @RequestMapping(value = "snowflakeIdTest", method = RequestMethod.POST)
    public AndiResponse<?> snowflakeIdTest() {
        return testService.snowflakeIdTest();
    }
}
