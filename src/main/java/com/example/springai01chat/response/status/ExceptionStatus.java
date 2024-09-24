package com.example.springai01chat.response.status;

@SuppressWarnings("all")
public enum ExceptionStatus {
    error001("error-001", "註冊失敗"),
    error002("error-002", "帳號重複"),
    error003("error-003", "註冊過程中發生錯誤"),
    error004("error-004", "登入失敗");


    protected String errorCode;
    protected String errorMessage;

    ExceptionStatus(String errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
