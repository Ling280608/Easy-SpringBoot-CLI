package com.ling.cli.aspect;

import com.ling.cli.annotation.DistributedLock;
import com.ling.cli.utils.ELUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import java.util.concurrent.TimeUnit;

@Slf4j
@Aspect
@Component
@ConditionalOnProperty(name = "spring.cache.type", havingValue = "redis", matchIfMissing = true)
public class RedisLockAspect {

    @Autowired
    private RedissonClient redissonClient;

    @Around("@annotation(distributedLock)")
    public Object around(ProceedingJoinPoint joinPoint, DistributedLock distributedLock) throws Throwable {
        // 解析 prefix 和 key中的SpEL表达式
        String prefix = ELUtils.parseExpression(distributedLock.prefix(), joinPoint);
        String key = ELUtils.parseExpression(distributedLock.key(), joinPoint);
        String lockKey = prefix + key; // 组合成完整的锁 key
        long leaseTime = distributedLock.leaseTime();
        TimeUnit timeUnit = distributedLock.timeUnit();
        RLock lock = redissonClient.getLock(lockKey);

        log.info("start lock {}", lockKey);
        try {
            // 尝试获取锁
            if (lock.tryLock(distributedLock.waitTime(), leaseTime, timeUnit)) {
                // 获取到锁，执行方法
                return joinPoint.proceed();
            } else {
                // 未获取到锁，抛出异常或自定义处理逻辑
                throw new RuntimeException("Unable to acquire lock for key: " + key);
            }
        } finally {
            if (lock.isHeldByCurrentThread()) {
                log.info("end lock {}", lockKey);
                // 只有是当前线程才去释放锁
                lock.unlock();
            }
        }
    }

}

