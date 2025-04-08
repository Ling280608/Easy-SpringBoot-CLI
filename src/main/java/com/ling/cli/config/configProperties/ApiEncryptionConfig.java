package com.ling.cli.config.configProperties;

import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author ling
 * @description: TODO
 */

@Data
@Component
@ConfigurationProperties(prefix = "api-enc")
public class ApiEncryptionConfig {
    /**
     * 是否开启请求加密
     */
    private boolean requestEnable;
    /**
     * 是否开启响应加密
     */
    private boolean respondEnable;
    /**
     * 密钥头字段(如果开启加密,则请求头中必须携带该字段,否则不进行解密操作)
     */
    private String keyHeader;
    /**
     * 公钥
     */
    private String publicKey;
    /**
     * 私钥
     */
    private String privateKey;
}
