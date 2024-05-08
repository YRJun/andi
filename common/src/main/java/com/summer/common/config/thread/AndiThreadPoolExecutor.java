package com.summer.common.config.thread;

import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class AndiThreadPoolExecutor {

    private static final int DEFAULT_CORE_POOL_SIZE = 1 << 1; //aka 2
    private static final int DEFAULT_MAX_POOL_SIZE = 1 << 4; //aka 16
    private static final long DEFAULT_KEEP_ALIVE_TIME = 5 * 60 * 1000;
    private static final int DEFAULT_WORK_QUEUE_CAPACITY = 1024;

    @Bean("threadPoolExecutor")
    public MdcAwareThreadPoolExecutor threadPoolExecutor() {
        return new MdcAwareThreadPoolExecutor(
                DEFAULT_CORE_POOL_SIZE,
                DEFAULT_MAX_POOL_SIZE,
                DEFAULT_KEEP_ALIVE_TIME,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(DEFAULT_WORK_QUEUE_CAPACITY),
                new CustomThreadFactory(),
                new CallerRunsPolicy()
        );
    }

    /**
     * 线程工厂
     */
    private static class CustomThreadFactory implements ThreadFactory {
        private static final String THREAD_NAME_PREFIX = "andi-thread-";
        private final AtomicInteger threadCount = new AtomicInteger(1);

        @Override
        public Thread newThread(@NotNull Runnable r) {
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

