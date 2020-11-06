package com.wwjjbt.sob_blog_system_mp.service;

import com.wwjjbt.sob_blog_system_mp.pojo.TbUser;
import com.wwjjbt.sob_blog_system_mp.response.ResponseResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface TbLostUserService {
    void createCaptcha(HttpServletResponse response, HttpServletRequest request);

    ResponseResult doLogin(String captcha, TbUser user);
}
