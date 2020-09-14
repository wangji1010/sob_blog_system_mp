package com.wwjjbt.sob_blog_system_mp.service.impl;

import com.wwjjbt.sob_blog_system_mp.response.ResponseResult;
import com.wwjjbt.sob_blog_system_mp.utils.SendMail;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/*
* 发送异步服务
* */
@Service
public class TaskService {

    @Async
    public void sendEmailTaskCode(String verifyCode,String emailAddress){
        try {
            SendMail.sendEmailCode(verifyCode+"",emailAddress);
        }catch (Exception e){

        }
    }
}
