package com.summer.common.config.datasource;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * @author Renjun Yu
 * @description 数据源路由实现，根据key获取数据源
 * @date 2024/01/05 21:10
 */
@Slf4j
public class DynamicRoutingDataSource extends AbstractRoutingDataSource {
    @Override
    protected Object determineCurrentLookupKey() {
        return DataSourceHolder.getDataSourceKey();
    }
}
