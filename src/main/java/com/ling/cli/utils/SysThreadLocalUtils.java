package com.ling.cli.utils;

import com.ling.cli.models.global.SysThreadVar;
import lombok.Getter;

/**
 * @author ling
 * @description: 线程变量工具
 */

@Getter
public class SysThreadLocalUtils {

    private static final ThreadLocal<SysThreadVar> THREAD_LOCAL = new ThreadLocal<>();

    public static void set(SysThreadVar sysThreadVar) {
        THREAD_LOCAL.set(sysThreadVar);
    }

    public static SysThreadVar get() {
        return THREAD_LOCAL.get();
    }

}
