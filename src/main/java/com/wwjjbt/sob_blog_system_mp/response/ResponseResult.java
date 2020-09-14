package com.wwjjbt.sob_blog_system_mp.response;


import java.io.Serializable;

public class ResponseResult implements Serializable {
    private boolean success;
    private int code;
    private String message;
    private Object data;



    public ResponseResult(ResponseState responseState) {
        this.success = responseState.isSuccess();
        this.code = responseState.getCode();
        this.message = responseState.getMessage();
    }

    public static ResponseResult success(){

        return new ResponseResult(ResponseState.SUCCESS);
    }
    public static ResponseResult failed(){

        return new ResponseResult(ResponseState.FAILED);
    }

    public static ResponseResult error_403(){

        return new ResponseResult(ResponseState.ERROR_403);
    }
    public static ResponseResult error_404(){

        return new ResponseResult(ResponseState.ERROR_404);
    }
    public static ResponseResult error_504(){

        return new ResponseResult(ResponseState.ERROR_504);
    }
    public static ResponseResult error_505(){

        return new ResponseResult(ResponseState.ERROR_505);
    }

    public static ResponseResult failed(String message){
        ResponseResult responseResult = new ResponseResult(ResponseState.FAILED);
        responseResult.setMessage(message);
        return responseResult;
    }

    public static ResponseResult ACCOUNT_NOT_LOGIN(String msg){
        ResponseResult result = new ResponseResult(ResponseState.ACCOUNT_NOT_LOGIN);
        result.setMessage(msg);
        return result;
    }
    public static ResponseResult PERMISSION_FORBID(String msg){
        ResponseResult result = new ResponseResult(ResponseState.PERMISSION_FORBID);
        result.setMessage(msg);
        return result;
    }
    public static ResponseResult success(String msg){
        ResponseResult result = new ResponseResult(ResponseState.SUCCESS);
        result.setMessage(msg);
        return result;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

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

    public Object getData() {
        return data;
    }

    public ResponseResult setData(Object data) {
        this.data = data;
        return this;
    }
}
