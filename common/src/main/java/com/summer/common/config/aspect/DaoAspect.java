package com.summer.common.config.aspect;

import com.summer.common.config.datasource.DataSourceHolder;
import com.summer.common.config.datasource.DatasourceTag;
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
            DatasourceTag[] annotations = daoClass.getAnnotationsByType(DatasourceTag.class);
            if (annotations.length != 0) {
                log.info("--(DaoAspect)current datasource:[{}]--", annotations[0].value());
                DataSourceHolder.setDataSourceKey(annotations[0].value());
            }
            return invocation.proceed();
        } catch (Exception e) {
            log.error("--(DaoAspect) error--", e);
            return null;
        } finally {
            DataSourceHolder.removeDataSourceKey();
        }
    }
}
