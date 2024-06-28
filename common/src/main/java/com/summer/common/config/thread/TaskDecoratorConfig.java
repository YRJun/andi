package com.summer.common.config.thread;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskDecorator;
import org.springframework.core.task.support.ContextPropagatingTaskDecorator;

/**
 * @author Renjun Yu
 * @date 2024/06/20 17:16
 */
@Configuration
public class TaskDecoratorConfig {
    @Bean
    public TaskDecorator taskDecorator() {
        return new ContextPropagatingTaskDecorator();
    }
}
