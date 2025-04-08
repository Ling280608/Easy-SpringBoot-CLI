package com.ling.cli.config.handler;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import cn.hutool.crypto.symmetric.AES;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSON;
import com.ling.cli.config.configProperties.ApiEncryptionConfig;
import com.ling.cli.utils.HttpServletRequestUtils;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.nio.charset.StandardCharsets;
import java.security.PublicKey;

/**
 * @author ling
 * @description: 拦截Controller方法默认返回参数，统一处理返回值/响应体
 */
@Slf4j
@ControllerAdvice
public class SysResponseBodyAdvice implements ResponseBodyAdvice {

    @Autowired
    private ThreadPoolTaskExecutor threadPool;
    @Autowired
    private ApiEncryptionConfig encryptionConfig;
    Logger logger = LoggerFactory.getLogger("AllRequestLog");

    @Override
    public Object beforeBodyWrite(Object responseBody, MethodParameter methodParameter, MediaType mediaType, Class aClass,
                                  ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        String responseBodyJson = null;
        if (ObjectUtil.isNotEmpty(responseBody)) {
            responseBodyJson = JSON.toJSONString(responseBody);
        }

        JSONObject logData = JSONObject.from(JSONUtil.createObj()
                .set("requestURI", HttpServletRequestUtils.getRequestURI())
                .set("remoteIP", HttpServletRequestUtils.getRemoteIP())
                .set("headers", HttpServletRequestUtils.getHeaders())
                .set("requestBody", HttpServletRequestUtils.getBody())
                .set("responseBody", responseBodyJson)
                .set("method", serverHttpRequest.getMethod()));

        threadPool.execute(() -> {
            this.printLogger(logData);
        });

        // 加解密
        if (encryptionConfig.isRespondEnable()) {
            Object encResponseBody = this.dataEncryption(responseBody, serverHttpResponse);
            if (ObjectUtil.isNotEmpty(encResponseBody)) {
                return encResponseBody;
            }
        }

        return responseBody;
    }

    /**
     * @param responseBody       响应体
     * @param serverHttpResponse 响应对象
     * @description: 响应数据加密
     */
    private Object dataEncryption(Object responseBody, ServerHttpResponse serverHttpResponse) {
        if (ObjectUtil.isEmpty(responseBody)) {
            return null;
        }
        if (ObjectUtil.isEmpty(encryptionConfig.getKeyHeader())) {
            log.info("开启了响应加密，但未配置响应头属性值，请检查配置");
            return null;
        }
        String responseBodyStr = JSONObject.toJSONString(responseBody);
        // 生成一个16位随机字符串
        String randomKey = RandomUtil.randomString(16);
        AES aes = new AES("CBC", "PKCS7Padding", randomKey.getBytes(StandardCharsets.UTF_8), randomKey.getBytes(StandardCharsets.UTF_8));
        String encResponseBodyStr = aes.encryptHex(responseBodyStr);

        // AES密钥加密
        RSA rsa = new RSA(encryptionConfig.getPrivateKey(), encryptionConfig.getPublicKey());
        String encRandomKey = rsa.encryptHex(randomKey, KeyType.PublicKey);
        HttpHeaders headers = serverHttpResponse.getHeaders();
        headers.add(encryptionConfig.getKeyHeader(), encRandomKey);
        return encResponseBodyStr;
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

