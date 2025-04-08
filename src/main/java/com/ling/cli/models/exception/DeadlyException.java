package com.ling.cli.models.exception;

/**
 * @author ling
 * @description: 致命异常
 */
public class DeadlyException extends RuntimeException {
    public DeadlyException(String message) {
        super(message);
    }
}
