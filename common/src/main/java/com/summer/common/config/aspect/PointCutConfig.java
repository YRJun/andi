package com.summer.common.config.aspect;

import com.summer.common.dao.CommonAndiDAO;
import org.springframework.aop.aspectj.AspectJExpressionPointcutAdvisor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;

/**
 * @author Renjun Yu
 * @description pointCut绑定Aspect类的配置类
 * @date 2024/01/21 11:51
 */
@Component
public class PointCutConfig {
    @Value("${pointcut.controller.property}")
    private String controllerPointcut;

    @Value("${pointcut.dao.property}")
    private String daoPointcut;
    @Resource
    private CommonAndiDAO commonAndiDao;

    @Bean
    public AspectJExpressionPointcutAdvisor controllerPointcutConfig() {
        AspectJExpressionPointcutAdvisor advisor = new AspectJExpressionPointcutAdvisor();
        advisor.setExpression(controllerPointcut);
        advisor.setAdvice(new ControllerAspect(commonAndiDao));
        return advisor;
    }

    @Bean
    public AspectJExpressionPointcutAdvisor daoPointcutConfig() {
        AspectJExpressionPointcutAdvisor advisor = new AspectJExpressionPointcutAdvisor();
        advisor.setExpression(daoPointcut);
        advisor.setAdvice(new DaoAspect());
        return advisor;
    }
}