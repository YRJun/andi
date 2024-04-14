package com.summer.common.model.response;

import com.summer.common.constant.Constant;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;

import java.io.Serializable;

/**
 * @author Renjun Yu
 * @description 统一接口返回类型
 * @date 2024/01/05 21:10
 */
@Getter
@Schema(name = "接口响应对象")
public class AndiResponse<T> implements Serializable {
    public AndiResponse() {
    }

    public static final int RESPONSE_SUCCESS = 200;
    public static final String MSG_SUCCESS = "success";
    public static final int RESPONSE_FAIL = -1;
    public static final String MSG_FAIL = "fail";
    public static final int RESPONSE_BAD_PARAM = -1000;
    public static final int RESPONSE_UNAUTHORIZED = 401;
    public static final int RESPONSE_EXCEPTION = -1111;


    @Schema(name = "追踪流水号")
    private String traceId;

    @Schema(name = "spanId")
    private String spanId;

    @Schema(name = "响应码")
    private int code;

    @Schema(name = "响应描述")
    private String msg;

    @Schema(name = "响应对象")
    private T data;

    public AndiResponse<T> withTraceId(String traceId) {
        if (StringUtils.isNotBlank(traceId)) {
            this.traceId = traceId;
        }
        return this;
    }

    public AndiResponse<T> withDefaultTraceId() {
        final String traceId = MDC.get(Constant.TRACE_ID);
        if (StringUtils.isNotBlank(traceId)) {
            this.traceId = traceId.length() < 16 ? traceId :traceId.substring(0, 16);
        }
        return this;
    }

    public AndiResponse<T> withSpanId(String spanId) {
        if (StringUtils.isNotBlank(spanId)) {
            this.spanId = spanId;
        }
        return this;
    }

    public AndiResponse<T> withDefaultSpanId() {
        final String spanId = MDC.get(Constant.SPAN_ID);
        if (StringUtils.isNotBlank(spanId)) {
            this.spanId = MDC.get(Constant.SPAN_ID);
        }
        return this;
    }

    public AndiResponse<T> withCode(int code) {
        this.code = code;
        return this;
    }

    public AndiResponse<T> withMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public AndiResponse<T> withData(T data) {
        this.data = data;
        return this;
    }

    public static <T> AndiResponse<T> success() {
        return new AndiResponse<T>().withCode(RESPONSE_SUCCESS).withMsg(MSG_SUCCESS).withDefaultTraceId().withDefaultSpanId();
    }

    public static <T> AndiResponse<T> success(T data) {
        return new AndiResponse<T>().withCode(RESPONSE_SUCCESS).withMsg(MSG_SUCCESS).withData(data).withDefaultTraceId().withDefaultSpanId();
    }

    public static <T> AndiResponse<T> success(int code, String msg) {
        return new AndiResponse<T>().withCode(code).withMsg(msg).withDefaultTraceId().withDefaultSpanId();
    }

    public static <T> AndiResponse<T> success(int code, String msg, T data) {
        return new AndiResponse<T>().withCode(code).withMsg(msg).withData(data).withDefaultTraceId().withDefaultSpanId();
    }

    public static <T> AndiResponse<T> fail() {
        return new AndiResponse<T>().withCode(RESPONSE_FAIL).withMsg(MSG_FAIL).withDefaultTraceId().withDefaultSpanId();
    }

    public static <T> AndiResponse<T> fail(String msg, T data) {
        return new AndiResponse<T>().withCode(RESPONSE_FAIL).withMsg(msg).withData(data).withDefaultTraceId().withDefaultSpanId();
    }

    public static <T> AndiResponse<T> fail(T data) {
        return new AndiResponse<T>().withCode(RESPONSE_FAIL).withMsg(MSG_FAIL).withData(data).withDefaultTraceId().withDefaultSpanId();
    }
}
