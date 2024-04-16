package com.summer.common.config.datasource;

import com.summer.common.constant.Constant;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Renjun Yu
 * @description 切换数据源的注解，aspect读取value值确定数据源
 * @date 2024/01/05 21:10
 */
@Target(value = {ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface SwitchDataSource {
    String value() default Constant.DATASOURCE_ANDI;
}
