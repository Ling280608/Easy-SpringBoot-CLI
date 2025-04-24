package com.ling.cli.config.handler;

import cn.hutool.core.lang.Dict;
import com.ling.cli.models.exception.DeadlyException;
import com.ling.cli.models.exception.WarningException;
import com.ling.cli.models.global.ResponseCode;
import com.ling.cli.models.global.SysResult;
import com.ling.cli.utils.HttpServletRequestUtils;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.engine.path.NodeImpl;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.validation.ConstraintViolationException;
import javax.xml.bind.ValidationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 异常处理器
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {


    /**
     * 400 - Bad Request
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public SysResult handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        log.info(HttpServletRequestUtils.getAllRequestInfo());
        log.error("方法参数类型不匹配", e);
        return SysResult.error("方法参数类型不匹配");
    }

    /**
     * 400 - Bad Request
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public SysResult handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        log.info(HttpServletRequestUtils.getAllRequestInfo());
        log.error("缺少请求参数", e);
        return SysResult.error("缺少请求参数");
    }

    /**
     * 400 - Bad Request
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public SysResult handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.info(HttpServletRequestUtils.getAllRequestInfo());
        log.error("参数解析失败", e);
        return SysResult.error("参数解析失败");
    }


    /**
     * 405 - Method Not Allowed
     */
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public SysResult handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.info(HttpServletRequestUtils.getAllRequestInfo());
        log.error("不支持当前请求方法", e);
        return SysResult.error("不支持当前请求方法");
    }

    /**
     * 415 - Unsupported Media Type
     */
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public SysResult handleHttpMediaTypeNotSupportedException(Exception e) {
        log.info(HttpServletRequestUtils.getAllRequestInfo());
        log.error("不支持当前媒体类型", e);
        return SysResult.error("不支持当前媒体类型");
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public SysResult handleDuplicateKeyException(DuplicateKeyException e) {
        log.info(HttpServletRequestUtils.getAllRequestInfo());
        log.error(e.getMessage(), e);
        log.error(e.getMessage(), e);
        return SysResult.error(ResponseCode.WARNING, "数据库中已存在该记录");
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public SysResult handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        log.info(HttpServletRequestUtils.getAllRequestInfo());
        log.error(e.getMessage(), e);
        return SysResult.error(ResponseCode.WARNING, "数据异常");
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public SysResult handleMaxSizeException(MaxUploadSizeExceededException e) {
        log.info(HttpServletRequestUtils.getAllRequestInfo());
        log.error(e.getMessage(), e);
        return SysResult.error(ResponseCode.WARNING, "文件过大!");
    }

    /**
     * 验证参数param类型
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public SysResult handleConstraintViolationException(ConstraintViolationException e) {
        log.error(e.getMessage(), e);
        List<HashMap> result = new ArrayList<>();
        e.getConstraintViolations().forEach((constraintViolation) -> {
            PathImpl path = (PathImpl) constraintViolation.getPropertyPath();
            NodeImpl leafNode = path.getLeafNode();
            String leafNodeName = leafNode.getName();
            result.add(Dict.create().set("field", leafNodeName).set("msg", constraintViolation.getMessage()));
        });
        return SysResult.error(ResponseCode.WARNING, result.toString());
    }

    /**
     * 兜底验证ValidationException
     */
    @ExceptionHandler(ValidationException.class)
    public SysResult handleValidationException(ValidationException e) {
        log.error("参数验证失败", e);
        return SysResult.warning( e.getCause().getMessage());
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public SysResult handlerNoFoundException(Exception e) {
        log.error(e.getMessage(), e);
        return SysResult.error(ResponseCode.ERROR, "路径不存在，请检查路径是否正确");
    }

    @ExceptionHandler(Exception.class)
    public SysResult handleException(Exception e) {
        log.error(e.getMessage(), e);
        return SysResult.error(ResponseCode.WARNING, "服务器异常");
    }

    /**
     * @description: 警告提醒异常捕获
     */
    @ExceptionHandler(DeadlyException.class)
    public SysResult handleDeadlyException(Exception e) {
        log.error(e.getMessage(), e);
        return SysResult.error(ResponseCode.WARNING, e.getMessage());
    }

    /**
     * @description: 警告提醒异常捕获
     */
    @ExceptionHandler(WarningException.class)
    public SysResult handleWarningException(Exception e) {
        return SysResult.error(ResponseCode.WARNING, e.getMessage());
    }
}
