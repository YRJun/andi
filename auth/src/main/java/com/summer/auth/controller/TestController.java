package com.summer.auth.controller;

import com.alibaba.fastjson2.JSONObject;
import com.summer.auth.service.TestService;
import com.summer.common.model.response.AndiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * @author Renjun Yu
 * @description
 * @date 2024/01/20 14:12
 */
@Slf4j
@RestController
@RequestMapping("test-management")
@Tag(name = "TestController", description = "测试管理")
@RequiredArgsConstructor
public class TestController {

    private final TestService testService;

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

    @Operation(summary = "esRestTest", description = "esRest查询测试")
    @RequestMapping(value = "esRestTest", method = RequestMethod.POST)
    public AndiResponse<?> esRestTest(@RequestBody JSONObject dsl) {
        try {
            return testService.esRestTest(dsl.toString());
        } catch (Exception e) {
            log.error("esRestTest error", e);
            return AndiResponse.fail(e.getMessage());
        }
    }

    @Operation(summary = "bcryptTest", description = "加密测试")
    @RequestMapping(value = "bcryptTest", method = RequestMethod.POST)
    public AndiResponse<?> bcryptTest(Integer salt) {
        return testService.bcryptTest(salt);
//        try {
//            return testService.bcryptTest(salt);
//        } catch (Exception e) {
//            log.error("transactionalTest error", e);
//            return AndiResponse.fail(e.getMessage());
//        }
    }

    @Operation(summary = "completableFutureTest", description = "completableFuture测试")
    @RequestMapping(value = "completableFutureTest", method = RequestMethod.GET)
    public AndiResponse<?> completableFutureTest() {
        try {
            return testService.completableFutureTest();
        } catch (Exception e) {
            log.error("completableFutureTest error", e);
            return AndiResponse.fail(e.getMessage());
        }
    }

}
