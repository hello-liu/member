package com.moss.common.security;

import com.moss.common.enumeration.SignType;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;

/**
 * @author wangzexiong
 * @Description 签名工具类
 * @date 2018-07-27 16:22
 */
public class SignUtils {

    private static final Logger logger = LoggerFactory.getLogger(SignUtils.class);

    public static final String PARA_SEPARATOR = "&";
    public static final String PROPERTIES_SEPARATOR = "=";
    public static final String[] IGNORE_PROPERTIES = {"sign", "signType"};
    public static final SignType DEFAULT_SIGN_TYPE = SignType.RSA;
    public static final String DEFAULT_CHARSET = "UTF-8";
    public static final String CLASS_KEY_NAME = "class";

    /**
     * 把Map转换成字符串,把对象的每个字段转换成"属性=值"的形式,然后使用指定的分隔符把所有字段拼接起来
     * @param map 要转换的Map
     * @param separator 分隔符
     * @param ignoreProperties 要忽略的属性
     * @return
     * @throws Exception
     */
    @SuppressWarnings("Since15")
    public static String mapToString (Map<String, ?> map, String separator, String... ignoreProperties) throws Exception {
        //把Object对象转换成HashMap
        List<Map.Entry<String, ?>> list = new ArrayList<Map.Entry<String, ?>>(map.entrySet());
        //排序,把转换后的HashMap按照key进行排序
        list.sort(new Comparator<Map.Entry<String, ?>>() {
            @Override
            public int compare(Map.Entry<String, ?> o1, Map.Entry<String, ?> o2) {
                return o1.getKey().compareTo(o2.getKey());
            }
        });

        List ignoreList = ignoreProperties != null? Arrays.asList(ignoreProperties):null;
        List<String> strList = new ArrayList<String>();
        for (Map.Entry<String, ?> entry : list) {
            if (entry.getValue() != null && !entry.getValue().toString().isEmpty()) {
                if (ignoreList == null || !ignoreList.contains(entry.getKey())) {
                    strList.add(entry.getKey() + PROPERTIES_SEPARATOR + entry.getValue());
                }
            }
        }
        return StringUtils.join(strList, separator);
    }

    public static String mapToString (Map<String, ?> map) throws Exception {
        return mapToString(map, PARA_SEPARATOR, IGNORE_PROPERTIES);
    }

    /**
     * 把Object对象转换成字符串,把对象的每个字段转换成"属性=值"的形式,然后使用指定的分隔符把所有字段拼接起来
     * @param obj 要转换的对象
     * @param separator 分隔符
     * @param ignoreProperties 要忽略的属性名称
     * @return 转换后的字符串
     * @throws Exception
     */
    @SuppressWarnings("Since15")
    public static String objToString (Object obj, String separator, String... ignoreProperties) throws Exception {
        Map<String, String> map = BeanUtils.describe(obj);
        if (map.containsKey(CLASS_KEY_NAME)) {
            map.remove(CLASS_KEY_NAME);
        }
        return mapToString(map, separator, ignoreProperties);
    }

    public static String objToString (Object obj) throws Exception {
        return objToString(obj, PARA_SEPARATOR, IGNORE_PROPERTIES);
    }



    /**
     * 读取DER格式的密钥
     * @param keyPath 密钥路径
     * @return 密钥
     */
    public static byte[] readDerKey (String keyPath) {
        logger.info("read DER format key [{}]", keyPath);

        FileInputStream input = null;
        byte[] keyBytes = null;
        try {
            File file = new File(keyPath);
            keyBytes = new byte[(int) file.length()];
            input = new FileInputStream(file);
            input.read(keyBytes);
        } catch (IOException e) {
            logger.error("read DER format key error [{}]", keyPath, e);
        } finally {
            try {
                if (input != null) {
                    input.close();
                }
            } catch (IOException e) {
                logger.error("close file error", e);
            }
        }
        return keyBytes;
    }

    /**
     * 读取PEM格式的密钥
     * @param keyPath 密钥路径
     * @return 密钥
     */
    public static String readPemKey (String keyPath) {
        logger.info("read PEM format key [{}]", keyPath);

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
            logger.error("read PEM format key [{}]", keyPath, e);
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                logger.error("close file error");
            }
        }
        return key.toString();
    }

    public static String sign (String data, String signType, String privateKey, String charset) throws Exception {
        if (signType == null || signType.trim().isEmpty()) {
            signType = DEFAULT_SIGN_TYPE.getCode();
        }

        if (charset == null || charset.trim().isEmpty()) {
            charset = DEFAULT_CHARSET;
        }

        if (signType.equalsIgnoreCase(SignType.RSA.getCode())) {
            logger.debug("要签名的数据[{}]", data);
            return RSAUtils.signToBase64(data, privateKey, charset);
        } else {
            logger.error("不支持的签名算法[{}]", signType);
            throw new Exception("不支持的签名算法[" + signType + "]");
        }
    }

    public static String sign (String data, String privateKey, String charset) throws Exception {
        return sign(data, null, privateKey, charset);
    }

    public static String sign (String data, String privateKey) throws Exception {
        return sign(data, null, privateKey, null);
    }

    public static String sign (Map<String, ?> data, String signType, String privateKey, String charset) throws Exception {
        return sign(mapToString(data), signType, privateKey, charset);
    }

    public static String sign (Map<String, ?> data, String privateKey, String charset) throws Exception {
        return sign(mapToString(data), null, privateKey, charset);
    }

    public static String sign (Object data, String signType, String privateKey, String charset) throws Exception {
        return sign(objToString(data), signType, privateKey, charset);
    }

    public static String sign (Object data, String privateKey, String charset) throws Exception {
        return sign(objToString(data), null, privateKey, charset);
    }

    public static String sign (Object data, String privateKey) throws Exception {
        return sign(objToString(data), null, privateKey, null);
    }

    public static boolean verify(String data, String sign, String signType, String publicKey, String charset) throws Exception {
        if (signType == null || signType.trim().isEmpty()) {
            signType = DEFAULT_SIGN_TYPE.getCode();
        }

        if (charset == null || charset.trim().isEmpty()) {
            charset = DEFAULT_CHARSET;
        }

        if (signType.equalsIgnoreCase(SignType.RSA.getCode())) {
            logger.debug("要验签的数据[{}]", data);
            return RSAUtils.verifyFromBase64(data, sign, publicKey, charset);
        } else {
            logger.error("不支持的签名算法[{}]", signType);
            throw new Exception("不支持的签名算法[" + signType + "]");
        }
    }

    public static boolean verify(String data, String sign, String publicKey, String charset) throws Exception {
        return verify(data, sign, null, publicKey, charset);
    }

    public static boolean verify(String data, String sign, String publicKey) throws Exception {
        return verify(data, sign, null, publicKey, null);
    }

    public static boolean verify(Map<String, ?> data, String signType, String sign, String publicKey, String charset) throws Exception {
        return verify(mapToString(data), sign, signType, publicKey, charset);
    }

    public static boolean verify(Map<String, ?> data, String sign, String publicKey, String charset) throws Exception {
        return verify(mapToString(data), sign, null, publicKey, charset);
    }

    public static boolean verify(Object data, String sign, String signType, String publicKey, String charset) throws Exception {
        return verify(objToString(data), sign, signType, publicKey, charset);
    }

    public static boolean verify(Object data, String sign, String publicKey, String charset) throws Exception {
        return verify(objToString(data), sign, null, publicKey, charset);
    }

    public static boolean verify(Object data, String sign, String publicKey) throws Exception {
        return verify(objToString(data), sign, null, publicKey, null);
    }
}
