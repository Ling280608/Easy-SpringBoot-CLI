package com.ling.cli.config.handler;

import cn.hutool.core.lang.Dict;
import com.ling.cli.models.global.ResponseCode;
import com.ling.cli.models.global.SysResult;
import com.ling.cli.utils.HttpServletRequestUtil;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.engine.path.NodeImpl;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
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
import java.util.List;
import java.util.Map;

/**
 * 异常处理器
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    // /**
    //  * 处理自定义异常
    //  */
    // @ExceptionHandler(BusinessException.class)
    // public SysResult handleRRException(BusinessException e) {
    //     log.info(HttpServletRequestUtil.getAllRequestInfo());
    //     log.error("业务异常", e.getMsg());
    //     return SysResult.error(e.getCode() + "", e.getMsg());
    // }
    //
    //
    // /**
    //  * 400 - Bad Request
    //  */
    // @ResponseStatus(HttpStatus.BAD_REQUEST)
    // @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    // public SysResult handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
    //     log.info(HttpServletRequestUtil.getAllRequestInfo());
    //     log.error("方法参数类型不匹配", e);
    //     return SysResult.error("方法参数类型不匹配");
    // }
    //
    // /**
    //  * 400 - Bad Request
    //  */
    // @ResponseStatus(HttpStatus.BAD_REQUEST)
    // @ExceptionHandler(MissingServletRequestParameterException.class)
    // public SysResult handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
    //     log.info(HttpServletRequestUtil.getAllRequestInfo());
    //     log.error("缺少请求参数", e);
    //     return SysResult.error("缺少请求参数");
    // }
    //
    // /**
    //  * 400 - Bad Request
    //  */
    // @ResponseStatus(HttpStatus.BAD_REQUEST)
    // @ExceptionHandler(HttpMessageNotReadableException.class)
    // public SysResult handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
    //     log.info(HttpServletRequestUtil.getAllRequestInfo());
    //     log.error("参数解析失败", e);
    //     return SysResult.error("参数解析失败");
    // }
    //
    //
    // /**
    //  * 400 - Bad Request
    //  */
    // @ResponseStatus(HttpStatus.BAD_REQUEST)
    // @ExceptionHandler(ValidationException.class)
    // public SysResult handleValidationException(ValidationException e) {
    //     log.info(HttpServletRequestUtil.getAllRequestInfo());
    //     log.error("参数验证失败", e);
    //     return SysResult.error(e.getMessage());
    // }
    //
    // /**
    //  * 405 - Method Not Allowed
    //  */
    // @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    // @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    // public SysResult handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
    //     log.info(HttpServletRequestUtil.getAllRequestInfo());
    //     log.error("不支持当前请求方法", e);
    //     return SysResult.error("不支持当前请求方法");
    // }
    //
    // /**
    //  * 415 - Unsupported Media Type
    //  */
    // @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    // @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    // public SysResult handleHttpMediaTypeNotSupportedException(Exception e) {
    //     log.info(HttpServletRequestUtil.getAllRequestInfo());
    //     log.error("不支持当前媒体类型", e);
    //     return SysResult.error("不支持当前媒体类型");
    // }
    //
    // @ExceptionHandler(DuplicateKeyException.class)
    // public SysResult handleDuplicateKeyException(DuplicateKeyException e) {
    //     log.info(HttpServletRequestUtil.getAllRequestInfo());
    //     log.error(e.getMessage(), e);
    //     return SysResult.error("500", "数据库中已存在该记录");
    // }
    //
    // @ExceptionHandler(DataIntegrityViolationException.class)
    // public SysResult handleDataIntegrityViolationException(DataIntegrityViolationException e) {
    //     log.info(HttpServletRequestUtil.getAllRequestInfo());
    //     log.error(e.getMessage(), e);
    //     return SysResult.error("500", "数据异常");
    // }
    //
    // @ExceptionHandler(MaxUploadSizeExceededException.class)
    // public SysResult handleMaxSizeException(MaxUploadSizeExceededException e) {
    //     log.info(HttpServletRequestUtil.getAllRequestInfo());
    //     log.error(e.getMessage(), e);
    //     return SysResult.error("500", "文件过大!");
    // }
    //
    // @ResponseStatus(HttpStatus.UNAUTHORIZED)
    // @ExceptionHandler(NotLoginException.class)
    // public SysResult handleNotLoginException(NotLoginException e) {
    //     log.info(HttpServletRequestUtil.getAllRequestInfo());
    //     log.error(e.getMessage());
    //     return SysResult.error(ResponseCode.TOKEN_ABNORMAL, e.getMessage());
    // }
    //
    // @ExceptionHandler(SaTokenException.class)
    // public SysResult handleMaxSizeException(SaTokenException e) {
    //     log.error("uri：{}, httpMethod:{}, errMsg:{}", HttpServletRequestUtil.getRequestURI(), HttpServletRequestUtil.getRequest().getMethod(), e.getMessage());
    //     return SysResult.error("403", e.getMessage());
    // }
    //
    // /**
    //  * 验证bean类型
    //  */
    // @ExceptionHandler(MethodArgumentNotValidException.class)
    // public SysResult handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
    //     log.error(e.getMessage(), e);
    //     List<Map> SysResult = new ArrayList<>();
    //     e.getBindingSysResult().getFieldErrors().forEach((fieldError) -> {
    //         SysResult.add(Dict.create().set("field", fieldError.getField()).set("msg", fieldError.getDefaultMessage()));
    //     });
    //     return SysResult.error(SysResult);
    // }
    //
    // /**
    //  * 验证参数param类型
    //  */
    // @ExceptionHandler(ConstraintViolationException.class)
    // public SysResult handleConstraintViolationException(ConstraintViolationException e) {
    //     log.error(e.getMessage(), e);
    //     List<Map> SysResult = new ArrayList<>();
    //     e.getConstraintViolations().forEach((constraintViolation) -> {
    //         PathImpl path = (PathImpl) constraintViolation.getPropertyPath();
    //         NodeImpl leafNode = path.getLeafNode();
    //         String leafNodeName = leafNode.getName();
    //         SysResult.add(Dict.create().set("field", leafNodeName).set("msg", constraintViolation.getMessage()));
    //     });
    //     return SysResult.error(SysResult);
    // }
    //
    // /**
    //  * 兜底验证ValidationException
    //  */
    // @ExceptionHandler(javax.validation.ValidationException.class)
    // public SysResult handleValidationException(javax.validation.ValidationException e) {
    //     log.error(e.getMessage(), e);
    //     return SysResult.warning( e.getCause().getMessage());
    // }
    //
    //
    // @ExceptionHandler(NoHandlerFoundException.class)
    // public SysResult handlerNoFoundException(Exception e) {
    //     log.error(e.getMessage(), e);
    //     return SysResult.error("404", "路径不存在，请检查路径是否正确");
    // }
    //
    // @ExceptionHandler(Exception.class)
    // public SysResult handleException(Exception e) {
    //     log.error(e.getMessage(), e);
    //     return SysResult.error("500", "服务器异常");
    // }
}
