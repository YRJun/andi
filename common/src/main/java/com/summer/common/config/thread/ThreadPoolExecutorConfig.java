package com.summer.common.config.thread;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.jvm.ExecutorServiceMetrics;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Renjun Yu
 * @description
 * @date 2024/01/21 16:25
 */
@Configuration
public class ThreadPoolExecutorConfig {
    private static final Logger log = LoggerFactory.getLogger(ThreadPoolExecutorConfig.class);

    private static final int DEFAULT_CORE_POOL_SIZE = 1 << 1; // aka 2
    private static final int DEFAULT_MAX_POOL_SIZE = 1 << 4; // aka 16
    private static final long DEFAULT_KEEP_ALIVE_TIME = 60 * 1000;
    private static final int DEFAULT_WORK_QUEUE_CAPACITY = 1 << 7; // aka 128

    @Resource
    private MeterRegistry meterRegistry;

    @Bean("threadPoolExecutor")
    public MdcAwareThreadPoolExecutor threadPoolExecutor() {
        final MdcAwareThreadPoolExecutor threadPoolExecutor = new MdcAwareThreadPoolExecutor(
                DEFAULT_CORE_POOL_SIZE,
                DEFAULT_MAX_POOL_SIZE,
                DEFAULT_KEEP_ALIVE_TIME,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(DEFAULT_WORK_QUEUE_CAPACITY),
                new CustomThreadFactory(),
                new CallerRunsPolicy()
        );
        // 注册线程池的metrics
        ExecutorServiceMetrics.monitor(meterRegistry, threadPoolExecutor, "mdcAwareThreadPoolExecutor");
        return threadPoolExecutor;
    }

    /**
     * 线程工厂
     */
    private static class CustomThreadFactory implements ThreadFactory {
        private static final String THREAD_NAME_PREFIX = "andi-thread-";
        private final AtomicInteger threadCount = new AtomicInteger(1);
        @Override
        public Thread newThread(@Nonnull Runnable r) {
            Thread thread = new Thread(r, THREAD_NAME_PREFIX + threadCount.getAndIncrement());
            thread.setDaemon(false);
            return thread;
        }
    }

    /**
     * 拒绝策略
     */
    private static class CallerRunsPolicy implements RejectedExecutionHandler {
        public CallerRunsPolicy() {
        }
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
            log.warn("线程池满负荷,当前运行线程总数:{},活动线程数:{} 等待运行任务数:{}", e.getPoolSize(), e.getActiveCount(), e.getQueue().size());
            if (!e.isShutdown()) {
                r.run();
            }
        }
    }
}

