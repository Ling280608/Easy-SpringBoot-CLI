package com.ling.cli.config.mvc;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.CharsetUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author ling
 * @description: 构建可重复读取request的inputStream
 */
public class RepeatedlyRequestWrapper extends HttpServletRequestWrapper {
    private  byte[] body;

    /**
     * @param request  请求体
     * @param response 响应体
     * @description: 初始化包装类
     */
    public RepeatedlyRequestWrapper(HttpServletRequest request, ServletResponse response) throws IOException {
        super(request);
        request.setCharacterEncoding(CharsetUtil.UTF_8);
        response.setCharacterEncoding(CharsetUtil.UTF_8);
        // 取出请求数据  
        body = IoUtil.readBytes(request.getInputStream(), false);
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }

    /**
     * @description: 重写ServletRequest接口中的getInputStream方法
     */
    @Override
    public ServletInputStream getInputStream() throws IOException {
        final ByteArrayInputStream bais = new ByteArrayInputStream(body);
        return new ServletInputStream() {
            @Override
            public int read() throws IOException {
                return bais.read();
            }

            @Override
            public int available() throws IOException {
                return body.length;
            }

            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {

            }
        };
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }
}