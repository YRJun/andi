package com.summer.common.config;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import com.summer.common.constant.Constant;
import com.summer.common.util.JacksonUtils;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.core.Ordered;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.stereotype.Component;

import java.time.*;
import java.time.format.DateTimeFormatter;

/**
 * @author Renjun Yu
 * @description 规范化LocalDateTime、LocalDate、LocalTime输出成json时的格式
 * @date 2024/03/16 18:02
 */
@Component
public class JacksonConfig implements Jackson2ObjectMapperBuilderCustomizer, Ordered {

    /**
     * 容器收集所有Jackson2ObjectMapperBuilderCustomizer实现类，通过
     * {@link org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration.JacksonObjectMapperBuilderConfiguration#customize(Jackson2ObjectMapperBuilder,java.util.List)}
     * 方法统一对Jackson2ObjectMapperBuilder进行设置，从而实现定制ObjectMapper
     * @param builder the JacksonObjectMapperBuilder to customize
     */
    @Override
    public void customize(Jackson2ObjectMapperBuilder builder) {
        // JSR 310日期时间处理
        JavaTimeModule javaTimeModule = new JavaTimeModule();

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(Constant.DATETIME_FORMAT);
//        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
//        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(JacksonUtils.DATETIME_FORMAT));
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(JacksonUtils.DATETIME_FORMAT));

//        javaTimeModule.addSerializer(LocalDateTime.class, new JsonSerializer<>() {
//            @Override
//            public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
//                // 将LocalDateTime转换为东八区的ZonedDateTime
//                ZonedDateTime shanghaiDateTime = value.atZone(ZoneId.of("Asia/Shanghai"));
//                // 将东八区的ZonedDateTime转换为UTC的ZonedDateTime
//                gen.writeString(shanghaiDateTime
//                        .withZoneSameInstant(ZoneId.of("UTC"))
//                        .format(JacksonUtils.ISO_8601_DATETIME_FORMAT));
//            }
//        });
//
//        javaTimeModule.addDeserializer(LocalDateTime.class, new JsonDeserializer<>() {
//            @Override
//            public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
//                return LocalDateTime.parse(p.getValueAsString(), JacksonUtils.ISO_8601_DATETIME_FORMAT);
//            }
//        });

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(Constant.DATE_FORMAT);
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ISO_LOCAL_DATE));
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ISO_LOCAL_DATE));

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern(Constant.TIME_FORMAT);
        javaTimeModule.addSerializer(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ISO_LOCAL_TIME));
        javaTimeModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ISO_LOCAL_TIME));

        builder.modules(javaTimeModule);

        /// 全局转化Long类型为String，解决序列化后传入前端Long类型精度丢失问题
        //builder.serializerByType(BigInteger.class, ToStringSerializer.instance);
        //builder.serializerByType(Long.class, ToStringSerializer.instance);

        //builder.timeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        //OBJECT_MAPPER = builder.build();
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
