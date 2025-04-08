package com.ling.cli.config.mvc;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import cn.hutool.crypto.symmetric.AES;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import cn.hutool.extra.spring.SpringUtil;
import com.ling.cli.config.configProperties.ApiEncryptionConfig;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * @author ling
 * @description: 构建可重复读取request的inputStream
 */
@Slf4j
public class RepeatedlyRequestWrapper extends HttpServletRequestWrapper {
    private byte[] body;

    /**
     * @param request  请求体
     * @param response 响应体
     * @description: 初始化包装类
     */
    public RepeatedlyRequestWrapper(HttpServletRequest request, ServletResponse response) throws IOException {
        super(request);
        request.setCharacterEncoding(CharsetUtil.UTF_8);
        response.setCharacterEncoding(CharsetUtil.UTF_8);

        // 解密模块
        ApiEncryptionConfig encryptionConfig = SpringUtil.getBean(ApiEncryptionConfig.class);
        if (encryptionConfig.isRequestEnable() && request.getMethod().equals("POST")) {
            // 获取 AES密钥 用 RSA解密
            String keyHeader = request.getHeader(encryptionConfig.getKeyHeader());
            if (ObjectUtil.isEmpty(keyHeader)) {
                log.warn("服务开启了请求加密，但未获取到AES密钥，请检查请求头是否包含{}", encryptionConfig.getKeyHeader());
                return;
            }

            RSA rsa = new RSA(encryptionConfig.getPrivateKey(),encryptionConfig.getPublicKey());
            byte[] decrypt = rsa.decrypt(keyHeader, KeyType.PrivateKey);
            String aesKey = new String(decrypt, StandardCharsets.UTF_8);
            log.info("解密后的AES密钥:{}", aesKey);

            // 取出请求数据
            body = IoUtil.readBytes(request.getInputStream(), false);
            String aesCiphertext = new String(body, StandardCharsets.UTF_8);

            //解密body
            AES aes = new AES("CBC", "PKCS7Padding", aesKey.getBytes(StandardCharsets.UTF_8), aesKey.getBytes(StandardCharsets.UTF_8));
            String requestData = aes.decryptStr(aesCiphertext);
            log.info("解密后的数据:{}", requestData);
            body = requestData.getBytes(StandardCharsets.UTF_8);
            return;
        }
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