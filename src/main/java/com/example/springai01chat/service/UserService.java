package com.example.springai01chat.service;

import com.example.springai01chat.dto.LoginUserDto;
import com.example.springai01chat.dto.RegisterUserDto;
import com.example.springai01chat.vo.UserVo;

public interface UserService {

    /**
     * 註冊用戶
     *
     * @param registerUserDto 用戶註冊資訊
     * @return 註冊是否成功
     */
    public Boolean saveUser(RegisterUserDto registerUserDto);

    /**
     * 檢查帳號是否存在
     *
     * @param userName 帳號
     * @return 用戶資訊
     */
    public UserVo findUserByUserName(String userName);


    public UserVo checkAccount(LoginUserDto loginUser);


}
