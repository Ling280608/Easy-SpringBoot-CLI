package com.ling.cli.models.exception;

/**
 * @author ling
 * @description: 警告异常
 */
public class WarningException extends RuntimeException {
    public WarningException(String message) {
        super(message);
    }
}
