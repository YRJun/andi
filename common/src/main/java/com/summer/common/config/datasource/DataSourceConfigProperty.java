package com.summer.common.config.datasource;

import com.zaxxer.hikari.HikariDataSource;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
/**
 * @author Renjun Yu
 * @description
 * @date 2024/01/05 21:10
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "hikaricp.common.properties")
public class DataSourceConfigProperty {
    @Value("${min-idle:1}")
    private int minIdle;
    @Value("${max-life-time:120000}")
    private int maxLifeTime;
    @Value("${idle-time-out:60000}")
    private int idleTimeOut;

    public HikariDataSource configDatasource(HikariDataSource dataSource) {
        dataSource.setMinimumIdle(this.minIdle);
        dataSource.setMaxLifetime(this.maxLifeTime);
        dataSource.setIdleTimeout(this.idleTimeOut);
        return dataSource;
    }
}
