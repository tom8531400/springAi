package com.example.springai01chat.security;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.DigestUtils;

public class CustomPasswordEncoder implements PasswordEncoder {
    private static final String SIGN = "o1eUaIvrs1NvytNfUR5whBdhs7cx0E";

    @Override
    public String encode(CharSequence rawPassword) {
        return customEncrypt(rawPassword.toString());
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        String rawPass = customEncrypt(rawPassword.toString());
        return rawPass.equals(encodedPassword);
    }

    private String customEncrypt(String password) {
        // 实现你自己的加密算法
        String pass = StringUtils.join(password, SIGN);
        return DigestUtils.md5DigestAsHex(pass.getBytes());
    }

}
