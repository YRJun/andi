package com.summer.common.util;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.json.JsonParseException;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.util.ReflectionUtils;

import java.util.concurrent.Callable;

/**
 * jackson工具类，仿照{@link org.springframework.boot.json.AbstractJsonParser AbstractJsonParser}
 * @author Renjun Yu
 * @date 2024/01/05 21:10
 */
public class JacksonUtils {
    private static ObjectMapper OBJECT_MAPPER;
    static {
        // new ObjectMapper()会不使用JacksonConfig的jackson配置
        OBJECT_MAPPER = new Jackson2ObjectMapperBuilder().build();
    }
    private static ObjectMapper getObjectMapper() {
        if (OBJECT_MAPPER == null) {
            OBJECT_MAPPER = new Jackson2ObjectMapperBuilder().build();
        }
        return OBJECT_MAPPER;
    }
    public static <T> T tryParse(Callable<T> parser) {
        return tryParse(parser, JacksonException.class);
    }
    public static <T> T tryParse(Callable<T> parser, Class<? extends Exception> clazz) {
        try {
            return parser.call();
        } catch (Exception ex) {
            if (clazz.isAssignableFrom(ex.getClass())) {
                throw new JsonParseException(ex);
            }
            ReflectionUtils.rethrowRuntimeException(ex);
            throw new IllegalStateException(ex);
        }
    }
    public static String writeValueAsString(Object value) {
        return tryParse(() -> getObjectMapper().writeValueAsString(value));
    }

    public static <T> T readValue(String content, Class<T> valueType) {
        return tryParse(() -> getObjectMapper().readValue(content, valueType));
    }
}
