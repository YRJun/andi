package com.summer.common.config.aspect;

import com.summer.common.constant.Constant;
import com.summer.common.dao.CommonAndiDAO;
import com.summer.common.model.andi.AndiInterfaceLog;
import com.summer.common.model.request.AndiBaseRequest;
import com.summer.common.model.response.AndiResponse;
import com.summer.common.util.JacksonUtils;
import com.summer.common.util.OtherUtils;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.annotation.AfterReturning;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author Renjun Yu
 * @description
 * @date 2024/01/21 11:49
 */
public class ControllerAspect implements MethodInterceptor {
    private static final Logger log = LoggerFactory.getLogger(ControllerAspect.class);

    public ControllerAspect() {
    }

    /**
     * 当使用 MethodInterceptor 接口实现类时，@Resource 注入的CommonAndiDao为 null,可能与代理的方式以及对象的生命周期有关
     */
    private CommonAndiDAO commonAndiDao;
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    public ControllerAspect(CommonAndiDAO commonAndiDao, ThreadPoolTaskExecutor threadPoolTaskExecutor) {
        this.commonAndiDao = commonAndiDao;
        this.threadPoolTaskExecutor = threadPoolTaskExecutor;
    }
    @Nullable
    @Override
    @AfterReturning
    public Object invoke(@Nonnull final MethodInvocation invocation) throws Throwable {
        final String methodFullPath = invocation.getMethod().getDeclaringClass().getName() + "." + invocation.getMethod().getName();
        final String username = "";
        log.info("收到接口调用请求:{}-{}", username, methodFullPath);
        final AndiBaseRequest andiBaseRequest = this.extractAndiBaseRequest(invocation, AndiBaseRequest.class);
        this.resetTraceIdAndSpanId(andiBaseRequest);
        final LocalDateTime startTime = LocalDateTime.now();
        Object response = null;
        try {
            response = invocation.proceed();
        }
        finally {
            RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
            threadPoolTaskExecutor.execute(new InsertInterfaceLog(commonAndiDao, methodFullPath, response, startTime, invocation, requestAttributes, username));
        }
        return response;
    }

    public void resetTraceIdAndSpanId(AndiBaseRequest request) {
        if (request == null) {
            return;
        }
        if (StringUtils.isNotBlank(request.getTraceId())) {
            MDC.put(Constant.TRACE_ID, request.getTraceId());
        }
        if (StringUtils.isNotBlank(request.getSpanId())) {
            MDC.put(Constant.SPAN_ID, request.getSpanId());
        }
    }

    /**
     * 检查入参有没有AndiBaseRequest类型，如果有就取出来，使用其中的traceId和spanId
     * @param invocation
     * @param clazz
     * @return
     * @param <T>
     */
    private <T> T extractAndiBaseRequest(MethodInvocation invocation, Class<T> clazz) {
        for (int i = 0; i < invocation.getArguments().length; i++) {
            Object argument = invocation.getArguments()[i];
            if (clazz.isInstance(argument)) {
                return clazz.cast(argument);
            }
        }
        log.debug("Method argument is not an instance of [{}]", clazz.getName());
        try {
            return clazz.cast(clazz.getDeclaredConstructor().newInstance());
        } catch (Exception e) {
            log.debug("No default constructor found for [{}]", clazz.getName());
        }
        return null;
    }

    private record InsertInterfaceLog(CommonAndiDAO commonAndiDao,
                                      String methodFullPath, Object response,
                                      LocalDateTime startTime, MethodInvocation invocation,
                                      RequestAttributes requestAttributes, String username) implements Runnable {
        @Override
        public void run() {
            commonAndiDao.insertInterfaceLog(createInterfaceLog(methodFullPath, response, startTime, invocation, requestAttributes, username));
        }
    }

    private static AndiInterfaceLog createInterfaceLog(String methodFullPath, Object response,
                                                       LocalDateTime startTime, MethodInvocation invocation,
                                                       RequestAttributes requestAttributes, String username) {
        final AndiInterfaceLog interfaceLog = new AndiInterfaceLog();
        interfaceLog.setEndTime(LocalDateTime.now());
        interfaceLog.setInterfaceName(methodFullPath);
        final Operation operation = invocation.getMethod().getAnnotation(Operation.class);
        interfaceLog.setInterfaceDesc(operation == null ? null : operation.description());
        final StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < invocation.getArguments().length; i++) {
            try {
                stringBuilder.append(JacksonUtils.writeValueAsString(invocation.getArguments()[i])).append(",");
            } catch (Exception e) {
                if (invocation.getArguments()[i] instanceof MultipartFile multipartFile) {
                    stringBuilder.append(multipartFile.getOriginalFilename()).append(",");
                } else if (invocation.getArguments()[i] instanceof HttpServletRequest httpServletRequest) {
                    stringBuilder.append(httpServletRequest.getRequestURI()).append(",");
                } else if (invocation.getArguments()[i] instanceof HttpServletResponse httpServletResponse) {
                    stringBuilder.append(httpServletResponse.getStatus()).append(",");
                } else {
                    log.error("接口日志:获取请求参数异常", e);
                }
            }
        }
        //请求参数的StringBuilder去掉最后一位的逗号
        if (!stringBuilder.isEmpty()) {
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        }
        interfaceLog.setRequest(stringBuilder.toString());
        int responseCode;
        String traceId;
        // 判断返回结果类型是AndiResponse还是ProblemDetail
        if (response instanceof AndiResponse<?> andiResponse) {
            responseCode = andiResponse.getCode();
            traceId = andiResponse.getTraceId();
        } else if (response instanceof ProblemDetail problemDetail) {
            responseCode = problemDetail.getStatus();
            log.info("接口日志:异常返回实体:{}", JacksonUtils.writeValueAsString(problemDetail));
            traceId = Objects.requireNonNull(problemDetail.getProperties(), "接口日志:异常返回实体缺少properties").get(Constant.TRACE_ID).toString();
        } else {
            responseCode = HttpStatus.UNPROCESSABLE_ENTITY.value();
            traceId = MDC.get(Constant.TRACE_ID);
            log.error("接口日志:获取响应类型异常,{}", JacksonUtils.writeValueAsString(response));
        }
        interfaceLog.setResponseCode(responseCode);
        interfaceLog.setUsername(username);
        // 获取当前请求的 RequestAttributes
        if (requestAttributes instanceof ServletRequestAttributes servletRequestAttributes) {
            // 如果是 Servlet 环境
            HttpServletRequest httpServletRequest = servletRequestAttributes.getRequest();
            interfaceLog.setIp(OtherUtils.getClientIP(httpServletRequest));
        }
        interfaceLog.setStartTime(startTime);
        interfaceLog.setTimeTaken((int) Duration.between(startTime, interfaceLog.getEndTime()).toMillis());
        interfaceLog.setTraceId(traceId);
        return interfaceLog;
    }
}
