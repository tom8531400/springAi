package com.example.springai01chat.service.Impl;

import cn.hutool.core.util.RandomUtil;
import com.example.springai01chat.dto.LoginUserDto;
import com.example.springai01chat.dto.RegisterUserDto;
import com.example.springai01chat.mapper.UserMapper;
import com.example.springai01chat.service.UserService;
import com.example.springai01chat.vo.UserVo;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Boolean saveUser(RegisterUserDto registerUserDto) {
        log.info("用戶資訊: {}", registerUserDto);
        String userName = registerUserDto.getUserName();
        UserVo userVo = findUserByUserName(userName);
        if (userVo != null) {
            log.info("此帳號{} 已被註冊過", userName);
            return false;
        }
        log.info("此帳號{} 尚未被註冊，進行註冊", userName);
        String password = registerUserDto.getPassword();
        String encode = passwordEncoder.encode(password);
        registerUserDto.setPassword(encode);
        registerUserDto.setUserId("USER-" + RandomUtil.randomString(10));
        return userMapper.insertUser(registerUserDto) == 1;
    }

    @Override
    public UserVo findUserByUserName(String userName) {
        log.info("需檢查的帳號: {}", userName);
        UserVo userVo = userMapper.findUserByUserName(userName);
        log.info("DB中對應的帳號: {}", userVo);
        return userVo;
    }

    @Override
    public UserVo checkAccount(LoginUserDto loginUser) {
        log.info("使用者輸入資訊: {}", loginUser);
        String userName = loginUser.getUserName();
        UserVo user = findUserByUserName(userName);
        if (user != null) {
            log.info("此帳號: {}已被註冊過", userName);
            String password = loginUser.getPassword();
            boolean result = passwordEncoder.matches(password, user.getPassword());
            return result ? user : null;
        }
        return null;
    }
}
