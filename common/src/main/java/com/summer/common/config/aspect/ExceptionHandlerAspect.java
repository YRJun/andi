package com.summer.common.config.aspect;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author Renjun Yu
 * @date 2024/08/16 17:35
 */
public class ExceptionHandlerAspect implements MethodInterceptor {
    private static final Logger log = LoggerFactory.getLogger(ExceptionHandlerAspect.class);

    @Nullable
    @Override
    public Object invoke(@Nonnull final MethodInvocation invocation) throws Throwable {
        final Object proceed = invocation.proceed();
        log.info("异常切面日志:{}", proceed);
        return proceed;
    }
}
