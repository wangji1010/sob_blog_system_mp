package com.wwjjbt.sob_blog_system_mp.service;

import com.wwjjbt.sob_blog_system_mp.pojo.TbUser;
import com.wwjjbt.sob_blog_system_mp.response.ResponseResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wangji
 * @since 2020-06-21
 */

public interface TbUserService  {

    ResponseResult initManagerCount(TbUser user, HttpServletRequest request);

    void createCaptcha(HttpServletResponse response,HttpServletRequest request)throws Exception;

    ResponseResult sendEmail(String type,HttpServletRequest request, String emailAddress);

    ResponseResult register(TbUser user, String emailCode,String captchaCode,HttpServletRequest request);

    ResponseResult doLogin(String captcha,TbUser user);

    ResponseResult getUserInfo(String userId);

    ResponseResult checkEmail(String email);

    ResponseResult checkUsername(String userName);

    ResponseResult updateUserInfo(HttpServletRequest request, HttpServletResponse response, String userId, TbUser user);

    TbUser checkUser(HttpServletRequest request,HttpServletResponse response);

    ResponseResult deleteUserById(String userId, HttpServletRequest request, HttpServletResponse response);

    ResponseResult getListUser(int page, int size,String userName,String email, HttpServletRequest request, HttpServletResponse response);

    ResponseResult updateUserPassword(String verifyCode, TbUser user);

    ResponseResult updateEmail( String email, String verifyCode,HttpServletRequest request,HttpServletResponse response);

    ResponseResult doLogout();

    ResponseResult parseToken(HttpServletRequest request,HttpServletResponse response);

    ResponseResult resetPassword(String userId, String password);
}
