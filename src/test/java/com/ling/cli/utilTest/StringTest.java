package com.ling.cli.utilTest;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;

/**
 * @author ling
 * @description: TODO
 */
public class StringTest {

    @Test
    public void test1() {
        String s = RandomUtil.randomString("-", 10);
    }

    @Test
    public void test2() {
        String content = "{\n" +
                "    \"parameterKey\": \"test\",\n" +
                "    \"parameterValue\": \"bbq\"\n" +
                "}";
        String keyMD5 = SecureUtil.md5("123456");
        System.out.println(keyMD5);

        // 构建
        SymmetricCrypto aes = new SymmetricCrypto(SymmetricAlgorithm.AES.getValue(), keyMD5.getBytes());

        // 加密
        // byte[] encrypt = aes.e(content);
        // String s = aes.encryptBase64(content);
        // System.out.println("encrypt:"+ s);
        // 解密
        // byte[] decrypt = aes.decrypt(encrypt);

        // 加密为16进制表示
        String encryptBase64 = aes.encryptBase64(content);
        // encryptHex = Base64.encode(encryptHex);
        System.out.println(encryptBase64);
        // 解密为字符串
        // String decryptStr = aes.decryptStr(encryptHex, CharsetUtil.CHARSET_UTF_8);
        // System.out.println(decryptStr);

    }
}
