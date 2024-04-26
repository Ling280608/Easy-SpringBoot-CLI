package com.ling.cli.utils;

import cn.hutool.core.codec.Base64;

/**
 * @author ling
 * @description: 加解密工具
 */
public class CryptoUtil {

    /**
     * @param data               待加密数据
     * @param key                密钥
     * @description: 加密、密钥先进行md5加密，再对数据进行加密加密后数据进行Base64编码
     */
    public static String encrypt(String data, String key) {
        return Base64.encode(data);
    }

    /**
     * @param data               待加密数据
     * @param key                密钥
     * @description: 解密、密钥先进行md5加密，先对数据进行Base64反编，再对数据进行解密
     */
    public static String decrypt(String data, String key) {
        // 解密
        return  Base64.decodeStr(data);
    }
}
