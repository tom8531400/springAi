package com.example.springai01chat.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RegisterUserDto {

    /**
     * 用戶編號
     */
    private String userId;

    /**
     * 用戶姓名
     */
    private String name;

    /**
     * 用戶帳號
     */
    @NotBlank(message = "Username 不可為空")
    @Size(min = 5, max = 20, message = "Username 須介於5~20之間")
    @Pattern(regexp = "^[a-zA-Z0-9\\s]+$", message = "輸入錯誤 只能填英數字")
    private String userName;

    /**
     * 用戶密碼
     */
    @NotBlank(message = "Username 不可為空")
    @Size(min = 5, max = 20, message = "Username 須介於5~20之間")
    @Pattern(regexp = "^[a-zA-Z0-9\\s]+$", message = "輸入錯誤 只能填英數字")
    private String password;

    /**
     * 年齡
     */
    private Integer age;

    /**
     * 性別
     */
    private String gender;

    /**
     * 電子郵件
     */
    private String email;

    /**
     * 居住縣市
     */
    private String city;

    /**
     * 手機號碼
     */
    private String phoneNumber;

    /**
     * 添加時間
     */
    private LocalDateTime createdAt;

    /**
     * 備註
     */
    private String notes;
}
