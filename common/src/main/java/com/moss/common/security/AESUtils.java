package com.moss.common.security;


import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;


/**
 * @author wangzexiong
 * @Description AES加密工具类
 * @date 2018-05-06 23:44
 */
public class AESUtils {

    public static final String AES_ALGORITHM = "AES";

    public static final String DEFAULT_MODE = "ECB";
    public static final String DEFAULT_PADDING = "PKCS5Padding";

    public static final Integer DEFAULT_KEY_SIZE = 128;


    public static String generateKey (Integer size) throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(AES_ALGORITHM);
        if (size != null) {
            keyGenerator.init(size);
        } else {
            keyGenerator.init(DEFAULT_KEY_SIZE);
        }
        SecretKey secretKey = keyGenerator.generateKey();
        byte[] key = secretKey.getEncoded();
        return Base64.encodeBase64String(key);
    }

    public static String generateKey () throws Exception {
        return generateKey(null);
    }

    /**
     * 通用密钥构建Key
     * @param key 密钥
     * @return
     */
    public static Key getKey (byte[] key) {
        return new SecretKeySpec(key, AES_ALGORITHM);
    }

    /**
     * 根据mode和padding生成加密算法字符串
     * @param mode 模式
     * @param padding 填充方法
     * @return
     */
    public static String getAlgorithms (String mode, String padding) {
        String algorithmsStr = AES_ALGORITHM;
        if (mode != null && !mode.isEmpty()) {
            algorithmsStr += "/" + mode;
        } else {
            algorithmsStr += "/" + DEFAULT_MODE;
        }

        if (padding != null && !padding.isEmpty()) {
            algorithmsStr += "/" + padding;
        } else {
            algorithmsStr += "/" + DEFAULT_PADDING;
        }
        return algorithmsStr;
    }

    /**
     * AES加密
     * @param key 密钥
     * @param data 待加密的数据
     * @param mode 模式
     * @param padding 填充方法
     * @return 加密后的数据
     * @throws Exception
     */
    public static byte[] encode (byte[] key, byte[] data, String mode, String padding) throws Exception {
        Cipher cipher = Cipher.getInstance (getAlgorithms(mode, padding));
        cipher.init (Cipher.ENCRYPT_MODE, getKey(key));
        return cipher.doFinal (data);
    }

    public static byte[] encode (String base64Key, byte[] data, String mode, String padding) throws Exception {
        return encode(Base64.decodeBase64(base64Key), data, mode, padding);
    }

    /**
     * AES解密
     * @param key 密钥
     * @param data 待解密的数据
     * @param mode 模式
     * @param padding 填充方法
     * @return 解密后的数据
     * @throws Exception
     */
    public static byte[] decode (byte[] key, byte[] data, String mode, String padding) throws Exception {
        Cipher cipher = Cipher.getInstance (getAlgorithms(mode, padding));
        cipher.init (Cipher.DECRYPT_MODE, getKey(key));
        return cipher.doFinal (data);
    }

    public static byte[] decode (String base64Key, byte[] data, String mode, String padding) throws Exception {
        return decode(Base64.decodeBase64(base64Key), data, mode, padding);
    }

    /**
     * AES加密，并把加密结果进行BASE64编码
     * @param key 密钥
     * @param data 待加密的数据
     * @param mode 模式
     * @param padding 填充方法
     * @return 加密结果
     * @throws Exception
     */
    public static String encodeToBase64 (byte[] key, byte[] data, String mode, String padding) throws Exception {
        return Base64.encodeBase64String (encode(key, data, mode, padding));
    }

    public static String encodeToBase64 (String base64Key, byte[] data, String mode, String padding) throws Exception {
        return Base64.encodeBase64String (encode(base64Key, data, mode, padding));
    }

    /**
     * AES加密，并把加密结果进行BASE64编码，使用默认模式ECB和默认填充方法PKCS5Padding
     * @param key 密钥
     * @param data 待加密的数据
     * @return 加密结果
     * @throws Exception
     */
    public static String encodeToBase64 (byte[] key, byte[] data) throws Exception {
        return Base64.encodeBase64String (encode(key, data, DEFAULT_MODE, DEFAULT_PADDING));
    }

    public static String encodeToBase64 (String base64Key, byte[] data) throws Exception {
        return Base64.encodeBase64String (encode(base64Key, data, DEFAULT_MODE, DEFAULT_PADDING));
    }

    /**
     * AES解密，直接从BASE64编码后的加密结果进行解密
     * @param key 密钥
     * @param data 待解密数据，BASE64编码
     * @param mode 模式
     * @param padding 填充方式
     * @return 解密结果
     * @throws Exception
     */
    public static byte[] decodeFromBase64 (byte[] key, String data, String mode, String padding) throws Exception {
        return decode (key, Base64.decodeBase64(data), mode, padding);
    }

    public static byte[] decodeFromBase64 (String base64Key, String data, String mode, String padding) throws Exception {
        return decode (base64Key, Base64.decodeBase64(data), mode, padding);
    }

    /**
     * AES解密，直接从BASE64编码后的加密结果进行解密，使用默认模式ECB和默认填充方式PKCS5Padding
     * @param key 密钥
     * @param data 待解密数据，BASE64编码
     * @return 解密结果
     * @throws Exception
     */
    public static byte[] decodeFromBase64 (byte[] key, String data) throws Exception {
        return decodeFromBase64 (key, data, null, null);
    }

    public static byte[] decodeFromBase64 (String base64Key, String data) throws Exception {
        return decodeFromBase64 (base64Key, data, null, null);
    }
}
