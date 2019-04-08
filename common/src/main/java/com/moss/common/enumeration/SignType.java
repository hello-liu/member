package com.moss.common.enumeration;

/**
 * @author wangzexiong
 * @Description 签名种类
 * @date 2016-5-22
 */
public enum SignType {
    /**
     * MD5
     */
    MD5("MD5"),
    /**
     * RSA
     */
    RSA("RSA"),
    /**
     * DSA
     */
    DSA("DSA");

    private String code;

    SignType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    @Override
    public String toString() {
        return code;
    }
}
