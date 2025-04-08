package com.ling.cli.utils;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @author ling
 * @description: Http请求体工具
 */
@Slf4j
public class HttpServletRequestUtils {
    /**
     * 获取request对象
     */
    public static HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    /**
     * 获取getRequestURI
     */
    public static String getRequestURI() {
        HttpServletRequest request = getRequest();
        String queryString = request.getQueryString();
        if (!StringUtils.hasText(queryString)) {
            return request.getRequestURI();
        }
        return request.getRequestURI() + "?" + queryString;
    }

    /**
     * 获取getRequestURI
     */
    public static UserAgent getRequestUserAgent() {
        HttpServletRequest request = getRequest();
        return UserAgentUtil.parse(request.getHeader("User-Agent"));
    }

    /**
     * 获取请求的地址
     */
    public static String getRemoteIP() {
        HttpServletRequest request = getRequest();
        if (ObjectUtil.isEmpty(request)) {
            return "unknown";
        }

        List<String> ipHeaders = Arrays.asList("x-forwarded-for", "Proxy-Client-IP", "X-Forwarded-For", "WL-Proxy-Client-IP", "X-Real-IP");
        String ip = null;
        for (String header : ipHeaders) {
            ip = request.getHeader(header);
            if (ObjectUtil.isNotEmpty(ip) && !"unknown".equalsIgnoreCase(ip)) {
                break;
            }
        }

        // 多次反向代理后会有多个IP值，第一个为真实IP。
        if (!ObjectUtil.isEmpty(ip)&& ip.contains(",")){
            int index = ip.indexOf(',');
            if (index != -1) {
                ip = ip.substring(0, index);
            }
        }

        return "0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : ip;
    }

    /**
     * 取得请求头信息 name:value
     */
    public static Map getHeaders() {
        HttpServletRequest request = getRequest();
        Map<String, String> map = new HashMap<>(32);
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            String value = request.getHeader(key);
            map.put(key, value);
        }
        return map;
    }

    /**
     * 获取请求体信息
     */
    public static String getBody() {
        HttpServletRequest request = getRequest();
        InputStream inputStream = null;
        try {
            inputStream = request.getInputStream();
            return StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
        } catch (IOException e) {
            log.error("HttpServletRequest.getBody", e);
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    log.error("HttpServletRequest.getBody", e);
                }
            }
        }
        return StrUtil.EMPTY;
    }

    public static String getAllRequestInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("RemoteAddress: ").append(getRemoteIP()).append(StrUtil.CRLF);
        sb.append("Method: ").append(getRequest().getMethod()).append(StrUtil.CRLF);
        sb.append("URI: ").append(getRequestURI()).append(StrUtil.CRLF);
        sb.append("Headers: ").append(StrUtil.join(StrUtil.CRLF + " ", mapToList(getHeaders()))).append(StrUtil.CRLF);
        sb.append("Body: ").append(getBody()).append(StrUtil.CRLF);
        return sb.toString();
    }

    private static List mapToList(Map parameters) {
        List parametersList = new ArrayList();
        parameters.forEach((name, value) -> {
            parametersList.add(name + "=" + value);
        });
        return parametersList;
    }

}
