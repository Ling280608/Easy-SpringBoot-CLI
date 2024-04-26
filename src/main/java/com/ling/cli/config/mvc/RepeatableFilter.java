package com.ling.cli.config.mvc;

import com.ling.cli.config.configProperties.ApiEncryptionConfig;
import com.ling.cli.utils.CryptoUtil;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author ling
 * @description: 过滤器，对请求进行处理前处理
 */
@Component
@EnableConfigurationProperties(ApiEncryptionConfig.class)
public class RepeatableFilter implements Filter {

    @Resource
    ApiEncryptionConfig apiEncryptionConfig;
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        System.out.println("RepeatableFilter");
        ServletRequest requestWrapper = null;
        if (request instanceof HttpServletRequest
                && StringUtils.startsWithIgnoreCase(request.getContentType(), MediaType.APPLICATION_JSON_VALUE)) {
            HttpServletRequest requestServlet = (HttpServletRequest) request;
            // 封装可重复读
            RepeatedlyRequestWrapper repeatedlyRequestWrapper = new RepeatedlyRequestWrapper(requestServlet, response);

            if (apiEncryptionConfig.isEnable() &&
                    requestServlet.getMethod().equals(RequestMethod.POST.name()) &&
                    requestServlet.getContentType().contains("application/json") ){
                // 接口解密
                String decrypt = CryptoUtil.decrypt(new String(repeatedlyRequestWrapper.getBody(), StandardCharsets.UTF_8), apiEncryptionConfig.getSecretKey());
                repeatedlyRequestWrapper.setBody(decrypt.getBytes(StandardCharsets.UTF_8));
            }
            byte[] body = repeatedlyRequestWrapper.getBody();
            String s = new String(body);
            requestWrapper = repeatedlyRequestWrapper;
        }
        if (null == requestWrapper) {
            chain.doFilter(request, response);
        } else {
            chain.doFilter(requestWrapper, response);
        }
    }

}