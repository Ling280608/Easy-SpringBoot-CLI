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
    // 是否开启加密
    private boolean enable;
    // 加密密钥
    private String secretKey;
    // 加密算法
    private SymmetricAlgorithm type;
}
