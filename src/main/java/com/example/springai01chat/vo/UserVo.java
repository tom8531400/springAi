package com.example.springai01chat.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserVo {
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
    private String userName;

    /**
     * 用戶密碼
     */
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
     * 狀態
     */
    private String status;

    /**
     * 上次登入時間
     */
    private LocalDateTime lastLoginTime;

    /**
     * 添加時間
     */
    private LocalDateTime createdAt;

    /**
     * 修改時間
     */
    private LocalDateTime updatedAt;

    /**
     * 修改人
     */
    private String updatedBy;

    /**
     * 備註
     */
    private String notes;
}
