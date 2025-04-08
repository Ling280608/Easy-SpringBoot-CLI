package com.ling.cli.exception;

/**
 * @author ling
 * @description: 客户端显示警告异常
 */
public class SysClientShowWarnException extends RuntimeException {
    public SysClientShowWarnException(String message) {
        super(message);
    }
}
