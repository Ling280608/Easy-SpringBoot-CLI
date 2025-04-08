package com.ling.cli.config.mvc;

import com.ling.cli.config.configProperties.ApiEncryptionConfig;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author ling
 * @description: 过滤器，对请求进行处理前处理
 */
@Component
public class RepeatableFilter implements Filter {

    @Resource
    ApiEncryptionConfig apiEncryptionConfig;
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        ServletRequest requestWrapper = null;
        if (request instanceof HttpServletRequest
                && StringUtils.startsWithIgnoreCase(request.getContentType(), MediaType.APPLICATION_JSON_VALUE)) {
            HttpServletRequest requestServlet = (HttpServletRequest) request;
            String encryptKey = ((HttpServletRequest) request).getHeader("Encrypt-Key");
            // 封装可重复读

            requestWrapper = new RepeatedlyRequestWrapper(requestServlet, response);
        }
        if (null == requestWrapper) {
            chain.doFilter(request, response);
        } else {
            chain.doFilter(requestWrapper, response);
        }
    }

}