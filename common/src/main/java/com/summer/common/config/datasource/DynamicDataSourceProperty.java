package com.summer.common.config.datasource;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Renjun Yu
 * @description
 * @date 2024/01/05 21:10
 */
@Data
@Slf4j
@Configuration
@ConfigurationProperties(prefix = "spring.dynamic-datasource", ignoreInvalidFields=true)
public class DynamicDataSourceProperty {
    /**
     * 多数据源数据库配置
     */
    public List<DataSourceConfigModel> dataSourceList = new ArrayList<>();

    @PostConstruct
    public void postConstruct() {
        log.info("--The number of dataSources:{}", CollectionUtils.isEmpty(dataSourceList) ? 0 : dataSourceList.size());
    }

}
