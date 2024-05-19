package com.summer.common.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.json.JsonParseException;

import java.util.concurrent.Callable;

/**
 * @author Renjun Yu
 * @description jackson工具类
 * @date 2024/01/05 21:10
 */
@Slf4j
public class JacksonUtils {
    private final static ObjectMapper OBJECT_MAPPER;
    static {
        //.registerModule(new JavaTimeModule())是为了支持java.time/date 对象，如LocalDate
        OBJECT_MAPPER = new ObjectMapper().registerModule(new JavaTimeModule());
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
