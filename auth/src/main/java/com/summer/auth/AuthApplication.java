package com.summer.auth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
/**
 * @author Renjun Yu
 * @description
 * @date 2024/01/06 16:05
 */
@SpringBootApplication(exclude = {ThymeleafAutoConfiguration.class, DataSourceAutoConfiguration.class})
@EnableFeignClients(basePackages = {"com.summer.common.feign"})
@EnableScheduling
@EnableAsync
@ComponentScan(basePackages = {"com.summer.common"})
@EnableAspectJAutoProxy(proxyTargetClass = true)
@Slf4j
public class AuthApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class, args);
        log.info("--Auth start complete!");
    }
}
