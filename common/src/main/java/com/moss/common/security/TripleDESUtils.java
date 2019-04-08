package com.moss.common.security;

import org.apache.commons.codec.binary.Base64;

/**
 * @author wangzexiong
 * @date 2018-05-06 0:34
 */
public class TripleDESUtils {

    public static final String ALGORITHM = DESUtils.DES3_ALGORITHM;

    public static String generateKey () throws Exception {
        return DESUtils.generateKey(ALGORITHM);
    }

    public static byte[] encode (byte[] key, byte[] data, String mode, String padding) throws Exception {
        return DESUtils.encode(key, data, ALGORITHM, mode, padding);
    }

    public static byte[] encode (String base64Key, byte[] data, String mode, String padding) throws Exception {
        return DESUtils.encode(base64Key, data, ALGORITHM, mode, padding);
    }

    public static byte[] decode (byte[] key, byte[] data, String mode, String padding) throws Exception {
        return DESUtils.decode(key, data, ALGORITHM, mode, padding);
    }

    public static byte[] decode (String base64Key, byte[] data, String mode, String padding) throws Exception {
        return DESUtils.decode(base64Key, data, ALGORITHM, mode, padding);
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
        return decodeFromBase64 (base64Key, data, null, null);
    }
}
