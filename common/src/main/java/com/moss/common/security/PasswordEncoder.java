package com.moss.common.security;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author wangzexiong
 * @Description 密码加密类
 * @date 2018-08-30 10:35
 */
public class PasswordEncoder implements org.springframework.security.crypto.password.PasswordEncoder {

    private static final Logger logger = LoggerFactory.getLogger(PasswordEncoder.class);

    private static final int SALT_SIZE = 10;
    private static final String SEPARATOR = "$";
    private static final int PASSWORD_FIELD_SIZE = 2;

    /**
     * 密码加密
     * @param rawPassword 原始密码
     * @return
     */
    @Override
    public String encode(CharSequence rawPassword) {
        String salt = RandomStringUtils.randomAlphanumeric(SALT_SIZE);
        byte[] digest = DigestUtils.sha256(rawPassword + salt);
        return salt + SEPARATOR + Base64.encodeBase64String(digest);
    }

    /**
     * 密码对比
     * @param rawPassword 原始密码
     * @param encodedPassword 加密后的密码
     * @return
     */
    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        String[] temp = encodedPassword.split("\\" + SEPARATOR);
        if (temp == null || temp.length < PASSWORD_FIELD_SIZE) {
            logger.error("EncodedPassword password format error");
            throw new RuntimeException("EncodedPassword password format error");
        }

        byte[] expected = Base64.decodeBase64(temp[1]);
        byte[] actual = DigestUtils.sha256(rawPassword + temp[0]);

        if (expected.length != actual.length) {
            return false;
        } else {
            int result = 0;

            for(int i = 0; i < expected.length; ++i) {
                result |= expected[i] ^ actual[i];
            }

            return result == 0;
        }
    }
}
