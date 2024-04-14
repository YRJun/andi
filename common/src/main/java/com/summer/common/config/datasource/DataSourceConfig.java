package com.summer.common.config.datasource;

import com.summer.common.constant.Constant;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Renjun Yu
 * @description
 * @date 2024/01/05 21:10
 */
@Configuration
@Slf4j
public class DataSourceConfig {
    @Resource
    private DynamicDataSourceProperty dynamicDataSourceProperty;

    @Resource
    private DataSourceConfigProperty dataSourceConfigProperty;

    @Bean("dynamicDataSource")
    public DataSource dynamicDataSource() {
        DataSource defaultDataSource = null;
        Map<Object, Object> dataSourceMap = new HashMap<>(8);
        for (DataSourceConfigModel dataSourceConfigModel : dynamicDataSourceProperty.getDataSourceList()) {
            HikariDataSource dataSource = DataSourceBuilder
                    .create()
                    .type(HikariDataSource.class)
                    .driverClassName(dataSourceConfigModel.getDriverClassName())
                    .url(dataSourceConfigModel.getUrl())
                    .username(dataSourceConfigModel.getUsername())
                    .password(dataSourceConfigModel.getPassword())
                    .build();
            dataSource.setPoolName(dataSourceConfigModel.getName());
            dataSource.setConnectionTestQuery(dataSourceConfigModel.getConnectionTestQuery());
            dataSourceMap.put(dataSourceConfigModel.getName(), dataSourceConfigProperty.configDatasource(dataSource));
            //默认数据源配置
            if (defaultDataSource == null || StringUtils.equals(Constant.DATASOURCE_ANDI, dataSourceConfigModel.getName())) {
                defaultDataSource = dataSource;
                log.info("--current default datasource:[{}]-[{}]", dataSource.getPoolName(), dataSource.getJdbcUrl());
            }
        }
        if (dataSourceMap.isEmpty()) {
            log.error("--Cannot found any datasource");
        }
        DynamicRoutingDataSource routingDataSource = new DynamicRoutingDataSource();
        Assert.notNull(defaultDataSource, "--Cannot found default datasource");
        routingDataSource.setDefaultTargetDataSource(defaultDataSource);
        routingDataSource.setTargetDataSources(dataSourceMap);
        log.info("--The number of configured datasource:{}", dataSourceMap.size());

        return routingDataSource;
    }
}
