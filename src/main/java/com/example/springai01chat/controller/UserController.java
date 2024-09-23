package com.example.springai01chat.controller;

import com.example.springai01chat.dto.LoginUserDto;
import com.example.springai01chat.dto.RegisterUserDto;
import com.example.springai01chat.response.ApiResponse;
import com.example.springai01chat.response.status.ExceptionStatus;
import com.example.springai01chat.service.UserService;
import com.example.springai01chat.vo.UserVo;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequestMapping(value = "/user")
@Validated
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private OpenAiChatModel openAiChatModel;

    /**
     * 用戶登入
     *
     * @param loginUser 帳號密碼
     * @return 是否成功登入
     */
    @RequestMapping(value = "/checkAccountAvailability", method = RequestMethod.POST)
    public ApiResponse<UserVo> checkAccountAvailability(@Valid @RequestBody LoginUserDto loginUser) {
        log.info("用戶輸入登入訊息: {}", loginUser);
        try {
            UserVo userVo = userService.checkAccount(loginUser);
            if (userVo != null) {
                return ApiResponse.success(HttpStatus.OK.value(), userVo);
            }
            return ApiResponse.failure(HttpStatus.BAD_REQUEST.value(), ExceptionStatus.error004.getErrorMessage());
        } catch (Exception ex) {
            log.info("用戶登入時發生錯誤: {}", ex.getMessage());
            return ApiResponse.failure(HttpStatus.INTERNAL_SERVER_ERROR.value(), ExceptionStatus.error003.getErrorMessage());
        }
    }

    /**
     * 用戶註冊
     *
     * @param registerUserDto 用戶註冊資訊
     * @return 註冊是否成功
     */
    @RequestMapping(value = "/registerUser", method = RequestMethod.POST)
    public ApiResponse<Boolean> registerUser(@Valid @RequestBody RegisterUserDto registerUserDto) {
        log.info("用戶輸入註冊訊息: {}", registerUserDto);
        try {
            boolean bool = userService.saveUser(registerUserDto);
            if (bool) {
                return ApiResponse.success(HttpStatus.OK.value(), true);
            }
            return ApiResponse.failure(HttpStatus.BAD_REQUEST.value(), ExceptionStatus.error001.getErrorMessage());
        } catch (Exception ex) {
            log.info("用戶註冊時發生異常: {}", ex.getMessage());
            return ApiResponse.failure(HttpStatus.INTERNAL_SERVER_ERROR.value(), ExceptionStatus.error003.getErrorMessage());
        }
    }

}
