package com.summer.gateway;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import reactor.core.publisher.Hooks;

/**
 * @author Renjun Yu
 * @description
 * @date 2024/01/06 11:02
 */
@SpringBootApplication(exclude = {ThymeleafAutoConfiguration.class, DataSourceAutoConfiguration.class})
@EnableFeignClients(basePackages = "com.summer.common.feign")
@EnableScheduling
@EnableAsync
@ComponentScan(basePackages = "com.summer.common")
@Slf4j
public class GatewayApplication {
    public static void main(String[] args) {
        //swagger地址：http://127.0.0.1:1000/swagger-ui/webjars/swagger-ui/index.html
        // 开启reactor的上下文传递
        Hooks.enableAutomaticContextPropagation();
        SpringApplication.run(GatewayApplication.class, args);
        log.info("--Gateway start complete!");
    }
}
