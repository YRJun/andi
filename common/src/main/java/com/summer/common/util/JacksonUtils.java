package com.summer.common.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.json.JsonParseException;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.util.concurrent.Callable;

/**
 * @author Renjun Yu
 * @description jackson工具类
 * @date 2024/01/05 21:10
 */
@Slf4j
public class JacksonUtils {
    private static final ObjectMapper OBJECT_MAPPER;
    static {
        final Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
        OBJECT_MAPPER = builder.build();
    }
    public static ObjectMapper getObjectMapper() {
        return OBJECT_MAPPER;
    }
    public static <T> T tryParse(Callable<T> parser) {
        return tryParse(parser, JsonParseException.class);
    }
    public static <T> T tryParse(Callable<T> parser, Class<? extends Exception> clazz) {
        try {
            return parser.call();
        } catch (Exception ex) {
            if (clazz.isAssignableFrom(ex.getClass())) {
                throw new JsonParseException(ex);
            }
            throw new IllegalStateException(ex);
        }
    }
    public static String enCode(Object value) {
        return tryParse(() -> getObjectMapper().writeValueAsString(value));
    }
}
