package com.summer.common.config.aspect;

import com.summer.common.constant.Constant;
import com.summer.common.dao.CommonAndiDAO;
import com.summer.common.model.andi.AndiInterfaceLog;
import com.summer.common.model.request.AndiBaseRequest;
import com.summer.common.model.response.AndiResponse;
import com.summer.common.util.JacksonUtils;
import com.summer.common.util.OtherUtils;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author Renjun Yu
 * @description
 * @date 2024/01/21 11:49
 */
@Slf4j
public class ControllerAspect implements MethodInterceptor {
    public ControllerAspect() {
    }
    //当使用 MethodInterceptor 接口实现类时，@Resource 注入的CommonAndiDao为 null 的原因可能与代理的方式以及对象的生命周期有关
    private CommonAndiDAO commonAndiDao;
    private ThreadPoolExecutor threadPoolExecutor;

    public ControllerAspect(CommonAndiDAO commonAndiDao, ThreadPoolExecutor threadPoolExecutor) {
        this.commonAndiDao = commonAndiDao;
        this.threadPoolExecutor = threadPoolExecutor;
    }
    @Nullable
    @Override
    public Object invoke(@Nonnull final MethodInvocation invocation) {
        final String methodFullPath = invocation.getMethod().getDeclaringClass().getName() + "." + invocation.getMethod().getName();
        final String username = "";
        log.info("收到接口调用请求:{}-{}", username, methodFullPath);
        final AndiBaseRequest andiBaseRequest = this.extractAndiBaseRequest(invocation, AndiBaseRequest.class);
        this.resetTraceIdAndSpanId(andiBaseRequest);

        final LocalDateTime startTime = LocalDateTime.now();
        AndiResponse<?> response = null;
        try {
            response = (AndiResponse<?>) invocation.proceed();
        } catch (Throwable e) {
            log.error("--(ControllerAspect) error", e);
            response = AndiResponse.fail("服务错误");
        } finally {
            RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
            final String responseStr = JacksonUtils.enCode(response);
            log.info("{}接口返回结果:{}" , methodFullPath, responseStr);
            threadPoolExecutor.execute(new InsertInterfaceLog(commonAndiDao, methodFullPath, response, startTime, invocation, requestAttributes, username));
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
        log.warn("Method argument is not an instance of {}", clazz.getName());
        try {
            return clazz.cast(clazz.getDeclaredConstructor().newInstance());
        } catch (Exception e) {
            log.debug("No default constructor found for {}", clazz.getName());
        }
        return null;
    }

    private record InsertInterfaceLog(CommonAndiDAO commonAndiDao,
                                      String methodFullPath, AndiResponse<?> response,
                                      LocalDateTime startTime, MethodInvocation invocation,
                                      RequestAttributes requestAttributes, String username) implements Runnable {
        @Override
        public void run() {
            commonAndiDao.createInterfaceLog(createInterfaceLog(methodFullPath, response, startTime, invocation, requestAttributes, username));
        }
    }

    private static AndiInterfaceLog createInterfaceLog(String methodFullPath, AndiResponse<?> response,
                                                       LocalDateTime startTime, MethodInvocation invocation,
                                                       RequestAttributes requestAttributes, String username) {
        final AndiInterfaceLog interfaceLog = new AndiInterfaceLog();
        interfaceLog.setEndTime(LocalDateTime.now());
        interfaceLog.setInterfaceName(methodFullPath);
        interfaceLog.setInterfaceDesc("");
        final Operation operation = invocation.getMethod().getAnnotation(Operation.class);
        if (operation != null) {
            interfaceLog.setInterfaceDesc(operation.description());
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < invocation.getArguments().length; i++) {
            try {
                stringBuilder.append(JacksonUtils.enCode(invocation.getArguments()[i])).append(",");
            } catch (Exception e) {
                if (invocation.getArguments()[i] instanceof MultipartFile multipartFile) {
                    stringBuilder.append(multipartFile.getOriginalFilename()).append(",");
                } else {
                    log.error("获取参数异常", e);
                }
            }
        }
        //请求参数的StringBuilder去掉最后一位的逗号
        if (!stringBuilder.isEmpty()) {
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        }
        interfaceLog.setRequest(stringBuilder.toString());
        interfaceLog.setResponseCode(response != null ? response.getCode() : AndiResponse.RESPONSE_FAIL);
        interfaceLog.setUsername(username);
        // 获取当前请求的 RequestAttributes
        if (requestAttributes instanceof ServletRequestAttributes servletRequestAttributes) {
            // 如果是 Servlet 环境
            HttpServletRequest httpServletRequest = servletRequestAttributes.getRequest();
            interfaceLog.setIp(OtherUtils.getClientIP(httpServletRequest));
        }
        interfaceLog.setStartTime(startTime);
        interfaceLog.setTimeTaken((int) Duration.between(startTime, interfaceLog.getEndTime()).toMillis());
        interfaceLog.setTraceId(response != null ? response.getTraceId() : MDC.get(Constant.TRACE_ID));
        return interfaceLog;
    }
}
