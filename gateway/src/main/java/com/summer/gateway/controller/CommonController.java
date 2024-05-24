package com.summer.gateway.controller;

import com.summer.common.model.andi.AndiUser;
import com.summer.common.model.request.AndiBaseRequest;
import com.summer.common.model.response.AndiResponse;
import com.summer.gateway.service.CommonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

/**
 * @author Renjun Yu
 * @description
 * @date 2024/01/06 11:52
 */
@RestController
@RequestMapping("common")
@Tag(name = "CommonController", description = "公共管理")
public class CommonController {
    @Resource
    private CommonService commonService;

    @Operation(summary = "trace", description = "获取traceId的测试方法")
    @PostMapping("trace")
    public AndiResponse<?> trace(@RequestBody AndiBaseRequest user) {
        return AndiResponse.success();
    }

    @Operation(summary = "test", description = "创建用户的测试方法")
    @PostMapping("test")
    public AndiResponse<?> test(@RequestBody AndiUser user) {
        return commonService.test(user);
    }
}
