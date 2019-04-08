package com.moss.common.security;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * @author wangzexiong
 * @date 2018-05-29
 */
public class KeyUtils {
    
    private static final Logger logger = LoggerFactory.getLogger(KeyUtils.class);

    /**
     * 读取PEM格式的密钥
     * @param keyPath 密钥路径
     * @return 密钥
     */
    public static String readPemKey (String keyPath) {
        StringBuffer key = new StringBuffer();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(keyPath));
            String str = null;
            boolean start = false;
            while (null != (str = reader.readLine())) {
                if (str.startsWith("-") && str.indexOf("BEGIN") >= 0) {
                    start = true;
                } else if (str.startsWith("-") && str.indexOf("END") >= 0) {
                    break;
                } else if (start) {
                    key.append(str);
                }
            }
        } catch (IOException e) {
            logger.error("读取PEM格式密钥出错[{}]", keyPath, e);
            throw new SecurityException("读取PEM格式密钥出错[keyPath=" + keyPath + "]");
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                logger.error("关闭输入流出错", e);
            }
        }
        return key.toString();
    }

    /**
     * 读取DER格式的密钥
     * @param keyPath 密钥路径
     * @return 密钥
     */
    public static byte[] readDerKey (String keyPath) {
        logger.info("读取DER格式密钥[{}]", keyPath);

        FileInputStream input = null;
        byte[] keyBytes = null;
        try {
            File file = new File(keyPath);
            keyBytes = new byte[(int) file.length()];
            input = new FileInputStream(file);
            input.read(keyBytes);
        } catch (IOException e) {
            logger.error("读取DER格式密钥出错[{}]", keyPath, e);
            throw new SecurityException("读取DER格式密钥出错[" + keyPath + "]");
        } finally {
            try {
                if (input != null) {
                    input.close();
                }
            } catch (IOException e) {
                logger.error("关闭输入流出错", e);
            }
        }
        return keyBytes;
    }

    /**
     * 从二进制格式的数据中获取私钥
     * @param privateKey 二进制格式私钥数据
     * @param algorithm 加密算法
     * @return privateKey 私钥
     */
    public static PrivateKey getPrivateKey (byte[] privateKey, String algorithm) {
        PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(privateKey);
        try {
            KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
            return keyFactory.generatePrivate(priPKCS8);
        } catch (NoSuchAlgorithmException e) {
            logger.error("读取私钥出错，不支持的算法[{}]", algorithm, e);
            throw new SecurityException("读取私钥出错，不支持的算法");
        } catch (InvalidKeySpecException e) {
            logger.error("无效的key类型", e);
            throw new SecurityException("无效的key类型");
        }
    }

    /**
     * 从Base64格式的数据中获取私钥
     * @param privateKey Base64格式私钥数据
     * @param algorithm 加密算法
     * @return privateKey 私钥
     */
    public static PrivateKey getPrivateKey (String privateKey, String algorithm) {
        return getPrivateKey(Base64.decodeBase64(privateKey), algorithm);
    }

    /**
     * 从二进制格式的数据中获取公钥
     * @param publicKey 二进制格式公钥数据
     * @param algorithm 加密算法
     * @return publicKey 公钥
     */
    public static PublicKey getPublicKey (byte[] publicKey, String algorithm) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
            return keyFactory.generatePublic(new X509EncodedKeySpec(publicKey));
        } catch (NoSuchAlgorithmException e) {
            logger.error("读取私钥出错，不支持的算法[{}]", algorithm, e);
            throw new SecurityException("读取私钥出错，不支持的算法");
        } catch (InvalidKeySpecException e) {
            logger.error("无效的key类型", e);
            throw new SecurityException("无效的key类型");
        }
    }

    /**
     * 从Base64格式的数据中获取公钥
     * @param publicKey Base64格式公钥数据
     * @param algorithm 加密算法
     * @return publicKey 公钥
     */
    public static PublicKey getPublicKey (String publicKey, String algorithm) {
        return getPublicKey(Base64.decodeBase64(publicKey), algorithm);
    }
}
