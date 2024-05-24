package com.summer.common.util;

import com.summer.common.constant.Constant;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.util.CollectionUtils;

import java.util.Map;

/**
 * @author Renjun Yu
 * @date 2024/05/07 16:47
 */
@Slf4j
public abstract class MdcTaskUtils {

    /**
     * 采用装饰者模式，装饰原生的 Runnable runnable 对象，在原生 Runnable 对象执行前，
     * 将父线程的 MDC 设置到子线程中，在afterExecute执行结束后，清除子线程 MDC 中的内容。
     * @param runnable runnable 对象
     * @param parentThreadContextMap 主线程上下文映射的副本
     * @return Runnable 对象
     */
    public static Runnable adaptMdcRunnable(Runnable runnable, Map<String, String> parentThreadContextMap) {
        return () -> {
            log.debug("parentThreadContextMap: {}, currentThreadContextMap: {}", parentThreadContextMap, MDC.getCopyOfContextMap());
            if (CollectionUtils.isEmpty(parentThreadContextMap) || !parentThreadContextMap.containsKey(Constant.TRACE_ID)) {
                log.debug("can not find a parentThreadContextMap, maybe task is fired using async or schedule task.");
                MDC.put(Constant.TRACE_ID, OtherUtils.getSnowflakeId());
            } else {
                MDC.put(Constant.TRACE_ID, parentThreadContextMap.get(Constant.TRACE_ID));
            }
            runnable.run();
        };
    }
}
