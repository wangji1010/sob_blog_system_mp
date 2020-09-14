package com.wwjjbt.sob_blog_system_mp.response;

import lombok.Data;



public enum  ResponseState {
    SUCCESS(true,200,"操作成功"),
    LOGIN_FAILED(false,401,"登录失败"),
    ACCOUNT_NOT_LOGIN(false,402,"账号未登录"),
    PERMISSION_FORBID(false,403,"无权限访问"),
    ERROR_404(false,404,"页面丢失"),
    ERROR_505(false,505,"无权限访问"),
    ERROR_504(false,504,"系统繁忙，请稍后重试"),
    ERROR_403(false,403,"无权限访问"),
    FAILED(false,400,"操作失败");


     ResponseState(boolean success,int code,String message){
        this.code = code;
        this.success=success;
        this.message=message;
    }

    private int code;
    private String message;
    private boolean success;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
