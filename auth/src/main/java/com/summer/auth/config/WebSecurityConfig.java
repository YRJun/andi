package com.summer.auth.config;

import com.summer.auth.config.security.CustomPasswordEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * @author Renjun Yu
 * @description security放行swagger配置类
 * @date 2024/02/05 15:43
 */

@Configuration
public class WebSecurityConfig {

    /**
     * 配置密码编码器
     * @return 自定义密码编码器
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new CustomPasswordEncoder();
    }
//    @Bean
//    public WebSecurityCustomizer webSecurityCustomizer() {
//        return (web) -> web
//                .ignoring()
//                .requestMatchers("/swagger-ui/**", "/v3/api-docs");
//    }
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        // 禁用csrf保护，才能放行post请求
//        http.csrf(AbstractHttpConfigurer::disable);
//        return http.build();
//    }
}

