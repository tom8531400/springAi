package com.example.springai01chat.util;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

@Log4j2
public class PasswordUtils {
    private static final String SIGN = "o1eUaIvrs1NvytNfUR5whBdhs7cx0E";

    public static String makePassword(String password) {
        log.info("加密前密碼: {}", password);
        String data = StringUtils.join(password, SIGN);
        String newPassword = DigestUtils.md5DigestAsHex(data.getBytes());
        log.info("加密後密碼: {}", newPassword);
        return newPassword;
    }

    public static boolean passwordValidat(String oldPassword, String newPassword) {
        log.info("加密前密碼: {}, DB中密碼: {}", oldPassword, newPassword);
        String pass = StringUtils.join(oldPassword, SIGN);
        String data = DigestUtils.md5DigestAsHex(pass.getBytes());
        log.info("加密後密碼: {}", data);
        return newPassword.equals(data);
    }
}
