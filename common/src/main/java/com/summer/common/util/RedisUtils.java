package com.summer.common.util;

import com.summer.common.model.response.AndiResponse;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Renjun Yu
 * @description
 * @date 2024/01/06 17:00
 */
@Component
@Slf4j
public class RedisUtils {
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Value("${spring.data.redis.lock-retry-count}")
    private int lockRetryCount;
    @Value("${spring.data.redis.lock-time}")
    private int lockTime;
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();

    /**
     * redis加锁
     * @param key key
     * @param value value
     * @param timeOut 加锁的时间
     * @param timeUnit 加锁的时间的单位
     * @return 是否加锁成功
     */
    public boolean lock(String key, String value, long timeOut, TimeUnit timeUnit) {
        try {
            final boolean isGetLock = Boolean.TRUE.equals(redisTemplate.opsForValue().setIfAbsent(key, value, timeOut, timeUnit));
            if (isGetLock) {
                condition.signalAll();
            }
            return isGetLock;
        } catch (Exception e) {
            log.error("--redis lock error", e);
            return false;
        }
    }

    /**
     * redis解锁
     * @param key key
     * @return 是否解锁成功
     */
    public boolean unlock(String key) {
        try {
            return Boolean.TRUE.equals(redisTemplate.opsForValue().getOperations().delete(key));
        } catch (Exception e) {
            log.error("--redis unlock error", e);
            return false;
        }
    }

    /**
     * 获取redis锁
     * @param key key
     * @param value value
     * @return 获取redis锁的结果
     */
    public AndiResponse<?> getRedisLock(String key, String value) {
        boolean isGetLock = false;
        try {
            lock.lock();
            for (int i = 0; i < lockRetryCount; i++) {
                //判断hasKey是因为要知道是锁被占用还是分配不了
                if (Boolean.FALSE.equals(redisTemplate.hasKey(key))) {
                    if (!(isGetLock = this.lock(key, value, lockTime, TimeUnit.MILLISECONDS))) {
                        if (i >= lockRetryCount - 1) {
                            log.info("获取redis锁失败,key:[{}]", key);
                            return AndiResponse.fail("获取redis锁失败,请稍后再试", null);
                        }
                    } else {
                        break;
                    }
                }
                condition.await(2000, TimeUnit.MILLISECONDS);
            }
            if (!isGetLock) {
                return AndiResponse.fail("有正在执行的操作,请稍后再试", null);
            }
            //获取锁成功
            return AndiResponse.success();
        } catch (InterruptedException e) {
            //恢复中断状态
            Thread.currentThread().interrupt();
            log.error("Thread interrupted while waiting for lock", e);
            return AndiResponse.fail("获取锁时发生中断异常", e);
        } catch (Exception e) {
            log.error("redis获取锁异常,key:[{}]", key, e);
            return AndiResponse.fail("系统异常", e);
        } finally {
            lock.unlock();
        }
    }
}
