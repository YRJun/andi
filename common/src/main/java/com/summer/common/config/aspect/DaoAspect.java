package com.summer.common.config.aspect;

import com.summer.common.config.datasource.DataSourceHolder;
import com.summer.common.config.datasource.SwitchDataSource;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author Renjun Yu
 * @description
 * @date 2024/01/21 15:19
 */
@Slf4j
public class DaoAspect implements MethodInterceptor {
    @Nullable
    @Override
    public Object invoke(@Nonnull final MethodInvocation invocation) throws Throwable {
        try{
            final Class<?> daoClass = invocation.getMethod().getDeclaringClass();
            final SwitchDataSource annotation = daoClass.getAnnotation(SwitchDataSource.class);
            if (annotation != null) {
                DataSourceHolder.setDataSourceKey(annotation.value());
            }
            return invocation.proceed();
        } finally {
            DataSourceHolder.removeDataSourceKey();
        }
    }
}
