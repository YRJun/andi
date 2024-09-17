package com.summer.auth.security.config;

import com.summer.auth.security.filter.TenantFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 将过滤器声明为 Spring bean 时要小心，无论是使用 @Component 注释还是在配置中将其声明为 bean，
 * 因为 Spring Boot 会自动将其注册到嵌入式容器中。这可能会导致过滤器被调用两次，一次由容器调用，
 * 一次由 Spring Security 调用，并且顺序不同。
 * 如果您仍想将过滤器声明为 Spring bean 以利用依赖注入，并避免重复调用，
 * 您可以通过声明 FilterRegistrationBean bean 并将其 enabled 属性设置为 false 来告诉 Spring Boot 不要将其注册到容器中
 * @author Renjun Yu
 * @date 2024/06/15 21:56
 */
//@Configuration
public class FilterRegistrationBeanConfig {
    //@Bean
    public FilterRegistrationBean<TenantFilter> tenantFilterRegistration(TenantFilter filter) {
        FilterRegistrationBean<TenantFilter> registration = new FilterRegistrationBean<>(filter);
        registration.setEnabled(false);
        return registration;
    }
}
