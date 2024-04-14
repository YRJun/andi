package com.summer.common.config.datasource;

/**
 * @author Renjun Yu
 * @description 动态数据源key
 * @date 2024/01/05 21:10
 */
public class DataSourceHolder {
    private static final ThreadLocal<String> DATA_SOURCE_KEY = new ThreadLocal<>();

    public static String getDataSourceKey() {
        return DATA_SOURCE_KEY.get();
    }

    public static void setDataSourceKey(String dataSourceName) {
        DATA_SOURCE_KEY.set(dataSourceName);
    }

    public static void removeDataSourceKey() {
        DATA_SOURCE_KEY.remove();
    }
}
