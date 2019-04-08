package com.moss.common.security;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 * @author wangzexiong
 * @date 2018-05-29 15:03
 */
public class RSAUtils {

    private static final Logger logger = LoggerFactory.getLogger(RSAUtils.class);

    public static final String KEY_ALGORITHM = "RSA";
    public static final String SIGN_ALGORITHMS = "SHA256WithRSA";
    public static final String CIPHER_ALGORITHM = "RSA/ECB/PKCS1Padding";
    public static final int RESERVE_BYTES = 11;
    public static final int DEFULT_KEY_SIZE = 2048;


    /**
     * 生成RSA密钥对
     * @param keySize 密钥长度
     * @return KeyPair
     * @throws Exception
     */
    public static KeyPair generateKeyPair (int keySize) throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        keyPairGenerator.initialize(keySize);
        return keyPairGenerator.generateKeyPair();
    }

    /**
     * 生成RSA密钥对，密钥长度为默认长度2048
     * @return KeyPair
     * @throws Exception
     */
    public static KeyPair generateKeyPair () throws Exception {
        return generateKeyPair(DEFULT_KEY_SIZE);
    }

    /**
     * 验证RSA公钥合法性
     * @param publicKey RSA公钥
     * @return
     */
    public static boolean checkPublicKey (byte[] publicKey) {
        if (publicKey == null || publicKey.length <= 0) {
            return false;
        }
        try {
            KeyUtils.getPublicKey(publicKey, KEY_ALGORITHM);
            return true;
        } catch (Exception e) {
            logger.error("RSA公钥合法性校验失败[{}]", e.getMessage(), e);
            return false;
        }
    }

    /**
     * 验证RSA公钥是否合法
     * @param publicKey Base64格式的RSA公钥
     * @return
     */
    public static boolean checkPublicKey (String publicKey) {
        if (StringUtils.isBlank(publicKey)) {
            return false;
        }
        return checkPublicKey(Base64.decodeBase64(publicKey));
    }

    /**
     * 验证RSA私钥是否合法
     * @param privateKey RSA私钥
     * @return
     */
    public static boolean checkPrivateKey (byte[] privateKey) {
        if (privateKey == null || privateKey.length <= 0) {
            return false;
        }
        try {
            KeyUtils.getPrivateKey(privateKey, KEY_ALGORITHM);
            return true;
        } catch (Exception e) {
            logger.error("RSA私钥合法性校验失败[{}]", e.getMessage(), e);
            return false;
        }
    }

    /**
     * 验证RSA私钥是否合法
     * @param privateKey BASE64格式的RSA私钥
     * @return
     */
    public static boolean checkPrivateKey (String privateKey) {
        if (StringUtils.isBlank(privateKey)) {
            return false;
        }
        return checkPrivateKey(Base64.decodeBase64(privateKey));
    }

    /**
     * 计算RSA签名
     * @param content 要签名的内容
     * @param privateKey 密钥
     * @return 签名结果
     */
    public static byte[] sign(byte[] content, PrivateKey privateKey) {
        try {
            Signature signature = Signature.getInstance(SIGN_ALGORITHMS);
            signature.initSign(privateKey);
            signature.update(content);
            return signature.sign();
        } catch (Exception e) {
            logger.error("计算RSA签名错误", e);
            throw new SecurityException("计算RSA签名错误", e);
        }
    }

    /**
     * 计算RSA签名，签名内容按照指定的编码格式进行解码
     * @param content 要签名的内容
     * @param privateKey 密钥
     * @param encode 编码格式
     * @return 签名结果
     */
    public static byte[] sign(String content, PrivateKey privateKey, String encode) {
        try {
            return sign(content.getBytes(encode), privateKey);
        } catch (UnsupportedEncodingException e) {
            logger.error("不支持的编码格式[{}]", encode, e);
            throw new SecurityException("不支持的编码格式[" + encode + "]", e);
        }
    }

    /**
     * 计算RSA签名，使用二进制格式的密钥
     * @param content 要签名的内容
     * @param privateKey 二进制格式的密钥
     * @return 签名结果
     */
    public static byte[] sign(byte[] content, byte[] privateKey) {
        return sign(content, KeyUtils.getPrivateKey(privateKey, KEY_ALGORITHM));
    }

    /**
     * 计算RSA签名，使用二进制格式的密钥，并按指定的编码格式对签名内容进行解码
     * @param content 要签名的内容
     * @param privateKey 二进制格式的密钥
     * @param encode 要签名内容的编码方式
     * @return 签名结果
     */
    public static byte[] sign(String content, byte[] privateKey, String encode) {
        return sign(content, KeyUtils.getPrivateKey(privateKey, KEY_ALGORITHM), encode);
    }

    /**
     * 计算RSA签名，使用Base64编码过的密钥
     * @param content 要签名的内容
     * @param privateKey Base64编码过的密钥
     * @return 签名结果
     */
    public static byte[] sign(byte[] content, String privateKey) {
        return sign(content, Base64.decodeBase64(privateKey));
    }

    /**
     * 计算RSA签名，使用Base64编码过的密钥，要签名的内容按指定编码进行解码
     * @param content 要签名的内容
     * @param privateKey Base64编码过的密钥
     * @param encode 要签名内容编码格式
     * @return 签名结果
     */
    public static byte[] sign(String content, String privateKey, String encode) {
        return sign(content, Base64.decodeBase64(privateKey), encode);
    }

    /**
     * 计算RSA签名，并将签名结果以进行Base64编码
     * @param content 要签名的内容
     * @param privateKey 私钥
     * @return Base64编码过的签名结果
     */
    public static String signToBase64(byte[] content, PrivateKey privateKey) {
        return Base64.encodeBase64String(sign(content, privateKey));
    }

    /**
     * 计算RSA签名，并将签名结果以进行Base64编码，要签名的内容按照指定的编码格式进行解码
     * @param content 要签名的内容
     * @param privateKey 私钥
     * @param encode 要签名内容的编码方式
     * @return Base64编码过的签名结果
     */
    public static String signToBase64(String content, PrivateKey privateKey, String encode) {
        return Base64.encodeBase64String(sign(content, privateKey, encode));
    }

    /**
     * 计算RSA签名，使用二进制格式的私钥，并将签名结果以进行Base64编码
     * @param content 要签名的内容
     * @param privateKey 二进制格式私钥
     * @return Base64编码过的签名结果
     */
    public static String signToBase64(byte[] content, byte[] privateKey) {
        return Base64.encodeBase64String(sign(content, privateKey));
    }

    /**
     * 计算RSA签名，使用二进制格式的私钥，并将签名结果以进行Base64编码，要签名的内容按照指定的编码格式进行解码
     * @param content 要签名的内容
     * @param privateKey 二进制格式私钥
     * @param encode 要签名内容的编码格式
     * @return Base64编码过的签名结果
     */
    public static String signToBase64(String content, byte[] privateKey, String encode) {
        return Base64.encodeBase64String(sign(content, privateKey, encode));
    }

    /**
     * 计算RSA签名，使用Base64编码过的私钥，并将签名结果以进行Base64编码
     * @param content 要签名的内容
     * @param privateKey 二进制格式私钥
     * @return Base64编码过的签名结果
     */
    public static String signToBase64 (byte[] content, String privateKey) {
        return Base64.encodeBase64String(sign(content, privateKey));
    }

    /**
     * 计算RSA签名，使用Base64编码过的私钥，并将签名结果以进行Base64编码，要签名的内容按照指定的编码格式进行解码
     * @param content 要签名的内容
     * @param privateKey 二进制格式私钥
     * @param encode 要签名内容的编码格式
     * @return Base64编码过的签名结果
     */
    public static String signToBase64 (String content, String privateKey, String encode) {
        return Base64.encodeBase64String(sign(content, privateKey, encode));
    }

    /**
     * 验证RSA签名
     * @param content 要验证内容
     * @param sign 签名
     * @param publicKey 公钥
     * @return 验证结果
     */
    public static boolean verify(byte[] content, byte[] sign, PublicKey publicKey) {
        try {
            Signature signature = Signature.getInstance(SIGN_ALGORITHMS);

            signature.initVerify(publicKey);
            signature.update(content);

            return signature.verify(sign);
        } catch (Exception e) {
            logger.error("RSA签名验证出错", e);
            throw new SecurityException("RSA签名验证出错", e);
        }
    }

    /**
     * 验证RSA签名，要验证的内容按照指定的编码格式进行解码
     * @param content 要验证的内容
     * @param sign 签名
     * @param publicKey 公钥
     * @param encode 要验证的内容编码格式
     * @return 验证结果
     */
    public static boolean verify(String content, byte[] sign, PublicKey publicKey, String encode) {
        try {
            return verify(content.getBytes(encode), sign, publicKey);
        } catch (UnsupportedEncodingException e) {
            logger.error("不支持的编码格式[{}]", encode, e);
            throw new SecurityException("不支持的编码格式[" + encode + "]");
        }
    }

    /**
     * 验证RSA签名，使用二进制格式的公钥
     * @param content 要验证的内容
     * @param sign 签名
     * @param publicKey 二进制格式的公钥
     * @return 验证结果
     */
    public static boolean verify (byte[] content, byte[] sign, byte[] publicKey) {
        return verify(content, sign, KeyUtils.getPublicKey(publicKey, KEY_ALGORITHM));
    }

    /**
     * 验证RSA签名，使用二进制格式的公钥，要验证的内容按照指定的编码格式进行解码
     * @param content 要验证的内容
     * @param sign 签名
     * @param publicKey 二进制格式的公钥
     * @param encode 要签名内容的编码格式
     * @return 验证结果
     */
    public static boolean verify (String content, byte[] sign, byte[] publicKey, String encode) {
        return verify(content, sign, KeyUtils.getPublicKey(publicKey, KEY_ALGORITHM), encode);
    }

    /**
     * 验证RSA签名，使用Base64编码格式的公钥
     * @param content 要验证的内容
     * @param sign 签名
     * @param publicKey Base64编码格式的公钥
     * @return 验证结果
     */
    public static boolean verify (byte[] content, byte[] sign, String publicKey) {
        return verify(content, sign, Base64.decodeBase64(publicKey));
    }

    /**
     * 验证RSA签名，使用Base64编码格式的公钥，要验证的内容按照指定的编码格式进行解码
     * @param content 要验证的内容
     * @param sign 签名
     * @param publicKey Base64编码格式的公钥
     * @param encode 要验证的内容编码格式
     * @return 验证结果
     */
    public static boolean verify (String content, byte[] sign, String publicKey, String encode) {
        return verify(content, sign, Base64.decodeBase64(publicKey), encode);
    }

    /**
     * 验证RSA签名，传入的签名使用Base64编码过
     * @param content 要验证的内容
     * @param sign 签名
     * @param publicKey 公钥
     * @return 验证结果
     */
    public static boolean verifyFromBase64 (byte[] content, String sign, PublicKey publicKey) {
        return verify(content, Base64.decodeBase64(sign), publicKey);
    }

    /**
     * 验证RSA签名，传入的内容进行Base64编码过，要验证的内容按照指定的编码格式进行解码
     * @param content 要验证的内容
     * @param sign 签名
     * @param publicKey 公钥
     * @param encode 要验证的内容编码格式
     * @return 验证结果
     */
    public static boolean verifyFromBase64 (String content, String sign, PublicKey publicKey, String encode) {
        return verify(content, Base64.decodeBase64(sign), publicKey, encode);
    }

    /**
     * 验证RSA签名，使用二进制格式的公钥，传入的内容进行Base64编码过
     * @param content 要验证的内容
     * @param sign 签名
     * @param publicKey 二进制格式公钥
     * @return 验证结果
     */
    public static boolean verifyFromBase64 (byte[] content, String sign, byte[] publicKey) {
        return verify(content, Base64.decodeBase64(sign), publicKey);
    }

    /**
     * 验证RSA签名，使用二进制格式的公钥，传入的内容进行Base64编码过，要验证的内容按照指定的编码格式进行解码
     * @param content 要验证的内容
     * @param sign 签名
     * @param publicKey 二进制格式公钥
     * @param encode 要验证的内容编码格式
     * @return 验证结果
     */
    public static boolean verifyFromBase64 (String content, String sign, byte[] publicKey, String encode) {
        return verify(content, Base64.decodeBase64(sign), publicKey, encode);
    }

    /**
     * 验证RSA签名，使用Base64编码格式的公钥，传入的内容进行Base64编码过
     * @param content 要验证的内容
     * @param sign 签名
     * @param publicKey Base64编码格式的公钥
     * @return 验证结果
     */
    public static boolean verifyFromBase64 (byte[] content, String sign, String publicKey) {
        return verify(content, Base64.decodeBase64(sign), publicKey);
    }

    /**
     * 验证RSA签名，使用Base64编码格式的公钥，传入的内容进行Base64编码过，要验证的内容按照指定的编码格式进行解码
     * @param content 要验证的内容
     * @param sign 签名
     * @param publicKey Base64编码格式的公钥
     * @param encode 要验证的内容编码格式
     * @return 验证结果
     */
    public static boolean verifyFromBase64 (String content, String sign, String publicKey, String encode) {
        return verify(content, Base64.decodeBase64(sign), publicKey, encode);
    }

    /**
     * 根据密钥长度计算加密块大小
     * @param keySize 密钥长度
     * @return 加密块大小
     */
    public static int getEncryptBlock (int keySize) {
        return (keySize / 8) - RESERVE_BYTES;
    }

    /**
     * 根据密钥长度计算解密块大小
     * @param keySize 密钥长度
     * @return 解密块大小
     */
    public static int getDecryptBlock (int keySize) {
        return keySize / 8;
    }

    /**
     * 使用RSA密码进行加密
     * @param data 待加密的数据
     * @param key 密钥
     * @return byte[] 加密后的数据
     */
    public static byte[] encrypt (byte[] data, Key key, int keySize) {
        try {
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            // 对数据分段加密
            int blockSize = getEncryptBlock(keySize);
            ByteArrayOutputStream outbuf = new ByteArrayOutputStream();
            for (int offset = 0; offset < data.length; ) {
                int inputLen = (data.length - offset);
                if (inputLen > blockSize) {
                    inputLen = blockSize;
                }
                outbuf.write(cipher.doFinal(data, offset, inputLen));
                offset += blockSize;
            }
            outbuf.flush();
            byte[] encryptedData = outbuf.toByteArray();
            outbuf.close();
            return encryptedData;
        } catch (Exception e) {
            logger.error("RSA加密失败", e);
            throw new SecurityException("RSA加密失败", e);
        }
    }

    /**
     * RSA 解密
     * @param data 待解密的数据
     * @param key 密钥
     * @return byte[] 解密后的数据
     */
    public static byte[] decrypt(byte[] data, Key key, int keySize) {
        try {
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key);
            // 对数据分段解密
            int blockSize = getDecryptBlock(keySize);
            ByteArrayOutputStream outbuf = new ByteArrayOutputStream();
            for (int offset = 0; offset < data.length; ) {
                int inputLen = (data.length - offset);
                if (inputLen > blockSize) {
                    inputLen = blockSize;
                }
                outbuf.write(cipher.doFinal(data, offset, inputLen));
                offset += blockSize;
            }
            outbuf.flush();
            byte[] decryptedData = outbuf.toByteArray();
            outbuf.close();
            return decryptedData;
        } catch (Exception e) {
            logger.error("RSA解密出错", e);
            throw new SecurityException("RSA解密失败", e);
        }
    }

    /**
     * 使用私钥对数据进行RSA加密
     * @param data 待加密的数据
     * @param privateKey 私钥
     * @return byte[] 加密后的数据
     */
    public static byte[] encryptByPrivateKey (byte[] data, RSAPrivateKey privateKey) {
        int keySize = privateKey.getModulus().bitLength();
        return encrypt(data, privateKey, keySize);
    }

    /**
     * 使用二进制格式的私钥对数据进行RSA加密
     * @param data 待加密的数据
     * @param privateKey 二进制格式私钥
     * @return byte[] 加密结果
     */
    public static byte[] encryptByPrivateKey (byte[] data, byte[] privateKey) {
        RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) KeyUtils.getPrivateKey(privateKey, KEY_ALGORITHM);
        return encryptByPrivateKey(data, rsaPrivateKey);
    }

    /**
     * 使用Base64格式的私钥对数据进行加密
     * @param data 待加密的数据
     * @param privateKey Base64格式私钥
     * @return byte[] 加密结果
     */
    public static byte[] encryptByPrivateKey (byte[] data, String privateKey) {
        RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) KeyUtils.getPrivateKey(privateKey, KEY_ALGORITHM);
        return encryptByPrivateKey(data, rsaPrivateKey);
    }

    /**
     * 使用公钥对数据进行RSA加密
     * @param data 待加密的数据
     * @param publicKey 公钥
     * @return byte[] 加密结果
     */
    public static byte[] encryptByPublicKey(byte[] data, RSAPublicKey publicKey) {
        int keySize = publicKey.getModulus().bitLength();
        return encrypt(data, publicKey, keySize);
    }

    /**
     * 使用二进制格式的公钥对数据进行RSA加密
     * @param data 待加密的数据
     * @param publicKey 二进制格式公钥
     * @return byte[] 加密结果
     */
    public static byte[] encryptByPublicKey(byte[] data, byte[] publicKey) {
        RSAPublicKey rsaPublicKey = (RSAPublicKey) KeyUtils.getPublicKey(publicKey, KEY_ALGORITHM);
        return encryptByPublicKey(data, rsaPublicKey);
    }

    /**
     * 使用Base64格式公钥对数据进行RSA加密
     * @param data 待加密的数据
     * @param publicKey Base64格式公钥
     * @return byte[] 加密结果
     */
    public static byte[] encryptByPublicKey(byte[] data, String publicKey) {
        RSAPublicKey rsaPublicKey = (RSAPublicKey) KeyUtils.getPublicKey(publicKey, KEY_ALGORITHM);
        return encryptByPublicKey(data, rsaPublicKey);
    }

    /**
     * 使用私钥进行解密
     * @param data 待解密的数据
     * @param privateKey 私钥
     * @return byte[] 解密结果
     */
    public static byte[] decryptByPrivateKey (byte[] data, RSAPrivateKey privateKey) {
        int keySize = privateKey.getModulus().bitLength();
        return decrypt(data, privateKey, keySize);
    }

    /**
     * 使用二进制格式的私钥进行解密
     * @param data 待解密的数据
     * @param privateKey 二进制格式私钥
     * @return byte[] 解密结果
     */
    public static byte[] decryptByPrivateKey (byte[] data, byte[] privateKey) {
        RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) KeyUtils.getPrivateKey(privateKey, KEY_ALGORITHM);
        return decryptByPrivateKey(data, rsaPrivateKey);
    }

    /**
     * 使用Base64格式私钥进行解密
     * @param data 待解密的数据
     * @param privateKey Base64格式私钥
     * @return byte[] 解密结果
     */
    public static byte[] decryptByPrivateKey (byte[] data, String privateKey) {
        RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) KeyUtils.getPrivateKey(privateKey, KEY_ALGORITHM);
        return decryptByPrivateKey(data, rsaPrivateKey);
    }

    /**
     * 使用公钥对进行解密
     * @param data 待解密的数据
     * @param publicKey 公钥
     * @return byte[] 解密结果
     */
    public static byte[] decryptByPublicKey (byte[] data, RSAPublicKey publicKey) {
        int keySize = publicKey.getModulus().bitLength();
        return decrypt(data, publicKey, keySize);
    }

    /**
     * 使用二进制格式的公钥进行解密
     * @param data 待解密的数据
     * @param publicKey 二进制格式的公钥
     * @return byte[] 解密结果
     */
    public static byte[] decryptByPublicKey (byte[] data, byte[] publicKey) {
        RSAPublicKey rsaPublicKey = (RSAPublicKey) KeyUtils.getPublicKey(publicKey, KEY_ALGORITHM);
        return decryptByPublicKey(data, rsaPublicKey);
    }

    /**
     * 使用Base64格式的公钥进行解密
     * @param data 待解密的数据
     * @param publicKey Base64格式公钥
     * @return byte[] 解密结果
     */
    public static byte[] decryptByPublicKey (byte[] data, String publicKey) {
        RSAPublicKey rsaPublicKey = (RSAPublicKey) KeyUtils.getPublicKey(publicKey, KEY_ALGORITHM);
        return decryptByPublicKey(data, rsaPublicKey);
    }
}
