package com.summer.common.config.datasource;

/**
 * @author Renjun Yu
 * @description 数据源实体类
 * @date 2024/01/05 21:10
 */
public class DataSourceConfigModel {
    private String name;
    private String driverClassName;
    private String url;
    private String username;
    private String password;
    private String connectionTestQuery;

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(final String driverClassName) {
        this.driverClassName = driverClassName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(final String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public String getConnectionTestQuery() {
        return connectionTestQuery;
    }

    public void setConnectionTestQuery(final String connectionTestQuery) {
        this.connectionTestQuery = connectionTestQuery;
    }
}
