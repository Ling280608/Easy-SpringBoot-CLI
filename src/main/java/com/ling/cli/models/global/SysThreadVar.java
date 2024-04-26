package com.ling.cli.models.global;

import lombok.Data;

/**
 * @author ling
 * @description: 线程变量
 */
@Data
public class SysThreadVar {
    /**
     * 线程中用到什么变量在这里定义
     */
    private String threadName;
}
