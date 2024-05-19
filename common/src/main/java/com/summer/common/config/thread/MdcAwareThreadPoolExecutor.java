package com.summer.common.config.thread;

import com.summer.common.constant.Constant;
import com.summer.common.util.MdcTaskUtils;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

import java.util.Map;
import java.util.concurrent.*;

/**
 * @author Renjun Yu
 * @date 2024/05/08 10:39
 */
@Slf4j
public class MdcAwareThreadPoolExecutor extends ThreadPoolExecutor {

    public MdcAwareThreadPoolExecutor(final int corePoolSize, final int maximumPoolSize, final long keepAliveTime, final TimeUnit unit, final BlockingQueue<Runnable> workQueue, final ThreadFactory threadFactory, final RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
    }

    @Override
    public void execute(Runnable command) {
        Map<String, String> parentThreadContextMap = MDC.getCopyOfContextMap();
        super.execute(MdcTaskUtils.adaptMdcRunnable(command, parentThreadContextMap));
    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        try {
            super.afterExecute(r, t);
            if (t != null) {
                log.error("Exception in thread pool", t);
            }
        } finally {
            MDC.remove(Constant.TRACE_ID);
        }
    }
}
