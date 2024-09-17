package com.summer.common.util;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JsonParseException;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.Callable;

/**
 * jackson工具类，仿照{@link org.springframework.boot.json.AbstractJsonParser AbstractJsonParser}
 * @author Renjun Yu
 * @date 2024/01/05 21:10
 */
@Component
public class JacksonUtils {
    @Autowired
    private JacksonUtils(ObjectMapper objectMapper) {
        OBJECT_MAPPER = objectMapper;
    }
    public static final DateTimeFormatter ISO_8601_DATETIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    public static final DateTimeFormatter DATETIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");

    private static ObjectMapper OBJECT_MAPPER;

    private static ObjectMapper getObjectMapper() {
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

    /**
     * ISO8601格式的字段专用的序列化器类
     * <p>
     *     通过{@link com.fasterxml.jackson.databind.annotation.JsonSerialize JsonSerialize}使用
     * </p>
     */
    public static class LocalDateTimeIso8601Serializer extends JsonSerializer<LocalDateTime> {
        @Override
        public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            ZoneId shanghaiZoneId = ZoneId.of("Asia/Shanghai");
            // 将LocalDateTime转换为东八区的ZonedDateTime
            ZonedDateTime shanghaiDateTime = value.atZone(shanghaiZoneId);
            // 将东八区的ZonedDateTime转换为UTC的ZonedDateTime
            gen.writeString(shanghaiDateTime.withZoneSameInstant(ZoneId.of("UTC")).format(ISO_8601_DATETIME_FORMAT));
        }
    }

    /**
     * ISO8601格式的字段专用的反序列化器类
     * <p>
     *     通过{@link com.fasterxml.jackson.databind.annotation.JsonDeserialize JsonDeserialize}使用
     * </p>
     */
    public static class LocalDateTimeIso8601Deserializer extends JsonDeserializer<LocalDateTime> {
        @Override
        public LocalDateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
            return LocalDateTime.parse(jsonParser.getValueAsString(), DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        }
    }
}
