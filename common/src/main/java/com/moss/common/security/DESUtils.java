package com.moss.common.security;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.DESedeKeySpec;
import java.security.Key;
import java.security.spec.KeySpec;

/**
 * @author wangzexiong
 * @date 2018-05-05 22:09
 */
public class DESUtils {

    public static final String DES_ALGORITHM = "DES";
    public static final String DES3_ALGORITHM = "DESede";

    public static final String DEFAULT_MODE = "ECB";
    public static final String DEFAULT_PADDING = "PKCS5Padding";

    public static String generateKey (String algorithm) throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(algorithm);
        SecretKey secretKey = keyGenerator.generateKey();
        byte[] key = secretKey.getEncoded();
        return Base64.encodeBase64String(key);
    }

    public static String generateKey () throws Exception {
        return generateKey(DES_ALGORITHM);
    }

    public static Key getKey (byte[] key, String algorithm) throws Exception {
        KeySpec spec = null;
        if (algorithm.equalsIgnoreCase(DES_ALGORITHM)) {
            spec = new DESKeySpec(key);
        } else if (algorithm.equalsIgnoreCase(DES3_ALGORITHM)) {
            spec = new DESedeKeySpec(key);
        } else {
            throw new Exception("无法识别的算法[" + algorithm + "]");
        }

        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(algorithm);
        return keyFactory.generateSecret(spec);
    }

    public static String getAlgorithms (String algorithm, String mode, String padding) {
        String algorithmsStr = algorithm;
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

    public static byte[] encode (byte[] key, byte[] data, String algorithm, String mode, String padding) throws Exception {
        Cipher cipher = Cipher.getInstance (getAlgorithms(algorithm, mode, padding));
        cipher.init (Cipher.ENCRYPT_MODE, getKey(key, algorithm));
        return cipher.doFinal (data);
    }

    public static byte[] encode (String base64Key, byte[] data, String algorithm, String mode, String padding) throws Exception {
        return encode(Base64.decodeBase64(base64Key), data, algorithm, mode, padding);
    }

    public static byte[] decode (byte[] key, byte[] data, String algorithm, String mode, String padding) throws Exception {
        Cipher cipher = Cipher.getInstance (getAlgorithms(algorithm, mode, padding));
        cipher.init (Cipher.DECRYPT_MODE, getKey(key, algorithm));
        return cipher.doFinal (data);
    }

    public static byte[] decode (String base64Key, byte[] data, String algorithm, String mode, String padding) throws Exception {
        return decode(Base64.decodeBase64(base64Key), data, algorithm, mode, padding);
    }

    public static byte[] encode (byte[] key, byte[] data, String mode, String padding) throws Exception {
        return encode(key, data, DES_ALGORITHM, mode, padding);
    }

    public static byte[] encode (String base64Key, byte[] data, String mode, String padding) throws Exception {
        return encode(base64Key, data, DES_ALGORITHM, mode, padding);
    }

    public static byte[] decode (byte[] key, byte[] data, String mode, String padding) throws Exception {
        return decode(key, data, DES_ALGORITHM, mode, padding);
    }

    public static byte[] decode (String base64Key, byte[] data, String mode, String padding) throws Exception {
        return decode(base64Key, data, DES_ALGORITHM, mode, padding);
    }

    public static String encodeToBase64 (byte[] key, byte[] data, String mode, String padding) throws Exception {
        return Base64.encodeBase64String (encode(key, data, mode, padding));
    }

    public static String encodeToBase64 (String base64Key, byte[] data, String mode, String padding) throws Exception {
        return encodeToBase64(Base64.decodeBase64(base64Key), data, mode, padding);
    }

    public static String encodeToBase64 (byte[] key, byte[] data) throws Exception {
        return encodeToBase64 (key, data, null, null);
    }

    public static String encodeToBase64 (String base64Key, byte[] data) throws Exception {
        return encodeToBase64 (base64Key, data, null, null);
    }

    public static byte[] decodeFromBase64 (byte[] key, String data, String mode, String padding) throws Exception {
        return decode (key, Base64.decodeBase64(data), mode, padding);
    }

    public static byte[] decodeFromBase64 (String base64Key, String data, String mode, String padding) throws Exception {
        return decodeFromBase64 (Base64.decodeBase64(base64Key), data, mode, padding);
    }

    public static byte[] decodeFromBase64 (byte[] key, String data) throws Exception {
        return decodeFromBase64 (key, data, null, null);
    }

    public static byte[] decodeFromBase64 (String base64Key, String data) throws Exception {
        return decodeFromBase64 (Base64.decodeBase64(base64Key), data, null, null);
    }


}
