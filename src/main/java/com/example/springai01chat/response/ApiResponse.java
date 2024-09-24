package com.example.springai01chat.response;

import lombok.Data;

@Data
@SuppressWarnings("all")
public class ApiResponse<T> {
    private int code;
    private String message;
    private String responseStatus;
    private T data;

    public static <T> ApiResponse<T> success(int code) {
        ApiResponse response = new ApiResponse();
        response.setCode(code);
        return response;
    }

    public static <T> ApiResponse<T> success(int code, T data) {
        ApiResponse response = new ApiResponse();
        response.setCode(code);
        response.setData(data);
        return response;
    }

    public static <T> ApiResponse<T> failure(int code, String meessage) {
        ApiResponse response = new ApiResponse();
        response.setMessage(meessage);
        response.setCode(code);
        return response;
    }
}
