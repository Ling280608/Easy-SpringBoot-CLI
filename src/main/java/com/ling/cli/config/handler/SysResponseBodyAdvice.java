package com.ling.cli.config.handler;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson2.JSON;
import com.ling.cli.config.configProperties.ApiEncryptionConfig;
import com.ling.cli.models.global.SysResult;
import com.ling.cli.utils.CryptoUtil;
import com.ling.cli.utils.HttpServletRequestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * @author ling
 * @description: 拦截Controller方法默认返回参数，统一处理返回值/响应体
 */
@ControllerAdvice
@EnableConfigurationProperties(ApiEncryptionConfig.class)
public class SysResponseBodyAdvice implements ResponseBodyAdvice {

    @Autowired
    private ThreadPoolTaskExecutor thread;
    @Autowired
    private ApiEncryptionConfig apiEncryptionConfig;
    Logger logger = LoggerFactory.getLogger("AllRequestLog");

    @Override
    public Object beforeBodyWrite(Object responseBody, MethodParameter methodParameter, MediaType mediaType, Class aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        String responseBodyJson = null;
        if (ObjectUtil.isNotEmpty(responseBody)) {
            responseBodyJson = JSON.toJSONString(responseBody);
        }

        JSONObject logData = JSONUtil.createObj()
                .set("requestURI", HttpServletRequestUtil.getRequestURI())
                .set("remoteIP", HttpServletRequestUtil.getRemoteIP())
                .set("headers", HttpServletRequestUtil.getHeaders())
                .set("requestBody", HttpServletRequestUtil.getBody())
                .set("responseBody", responseBodyJson)
                .set("method", serverHttpRequest.getMethod());

        thread.execute(() -> {
            this.printLogger(logData);
        });

        // 数据加密
        if (apiEncryptionConfig.isEnable()){
            return CryptoUtil.encrypt(JSON.toJSONString(responseBody), apiEncryptionConfig.getSecretKey());
        }
        return responseBody;
    }

    @Override
    public boolean supports(MethodParameter methodParameter, Class aClass) {
        return true;
    }

    public synchronized void printLogger(JSONObject logData) {
        Object userName = logData.get("userName");
        String headStr = String.format("--------------------[ %s ]-----------------", logData.get("requestURI"));
        logger.info(headStr);
        logger.info("发起请求客户端:[{}]", logData.get("remoteIP"));
        logger.info("请求方式:[{}]", logData.get("method"));
        logger.info("请求路径:[{}]", logData.get("requestURI"));
        logger.info("请求Body:{}", logData.get("requestBody"));
        logger.info("响应Body:{}", logData.get("responseBody"));
        logger.info("请求头:[{}]", logData.get("headers"));
        logger.info("{}}\n\n\n", RandomUtil.randomString("-", headStr.length()));
    }

}

