package com.example.springai01chat.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
public class LoginUserDto {

    @NotBlank(message = "Username 不可為空")
    @Size(min = 5, max = 20, message = "Username 須介於5~20之間")
    @Pattern(regexp = "^[a-zA-Z0-9\\s]+$", message = "輸入錯誤 只能填英數字")
    private String userName;

    @NotBlank(message = "Password 不可為空")
    @Size(min = 5, max = 20, message = "Password 須介於5~20之間")
    @Pattern(regexp = "^[a-zA-Z0-9\\s]+$", message = "輸入錯誤 只能填英數字")
    private String password;

}
