package com.summer.common.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author Renjun Yu
 * @description 基础接口请求对象
 * @date 2024/01/05 21:10
 */
@Data
@Schema(name = "基础接口请求对象")
public class AndiBaseRequest {
    @Schema(name = "traceId")
    private String traceId;
    @Schema(name = "spanId")
    private String spanId;
}
