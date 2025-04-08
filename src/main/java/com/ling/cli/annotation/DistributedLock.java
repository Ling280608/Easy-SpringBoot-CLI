package com.ling.cli.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DistributedLock {
    // 可以使用 SpEL 表达式动态生成前缀
    String prefix() default "";
    // 可以使用 SpEL 表达式动态生成key
    String key() default "defaultLock";
    // 锁的持有时间
    long leaseTime() default 10;
    // 持有时间单位
    TimeUnit timeUnit() default TimeUnit.SECONDS;
    // 锁的等待时间
    long waitTime();
}


