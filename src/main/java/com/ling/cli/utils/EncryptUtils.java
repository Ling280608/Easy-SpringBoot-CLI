package com.ling.cli.utils;

import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import cn.hutool.crypto.symmetric.SymmetricCrypto;

/**
 * @author ling
 * @description: 加解密工具
 */
public class EncryptUtils {

    public void aesEncrypt(String str){

    }

    public String aesDecrypt(String str,byte[] key){
        //构建
        SymmetricCrypto aes = new SymmetricCrypto(SymmetricAlgorithm.AES, key);
        // aes.encrypt();
    return null;
    }
}
