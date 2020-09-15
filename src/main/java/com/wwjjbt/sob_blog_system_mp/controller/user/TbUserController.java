package com.wwjjbt.sob_blog_system_mp.controller.user;


import com.sun.org.apache.regexp.internal.RE;
import com.wf.captcha.SpecCaptcha;
import com.wf.captcha.base.Captcha;
import com.wwjjbt.sob_blog_system_mp.pojo.TbUser;
import com.wwjjbt.sob_blog_system_mp.response.ResponseResult;
import com.wwjjbt.sob_blog_system_mp.service.TbUserService;
import com.wwjjbt.sob_blog_system_mp.utils.Constrants;
import com.wwjjbt.sob_blog_system_mp.utils.IdWorker;
import com.wwjjbt.sob_blog_system_mp.utils.RedisUtil;
import com.wwjjbt.sob_blog_system_mp.utils.TextUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.util.Random;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author wangji
 * @since 2020-06-21
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class TbUserController {


    @Autowired
    private TbUserService userService;

    @PostMapping("/admin")
    public ResponseResult initManagerAccount(@RequestBody TbUser user, HttpServletRequest request) {
        log.info("==========" + user.getUserName());
        log.info("==========" + user.getPassword());
        log.info("==========" + user.getEmail());
        return userService.initManagerCount(user, request);
    }

    /*
     * 注册
     * */
    @PostMapping("/register")
    public ResponseResult register(@RequestBody TbUser user
            , @RequestParam("email_code") String emailCode
            , @RequestParam("captcha_code") String captchaCode
            , HttpServletRequest request
    ) {


        return userService.register(user, emailCode, captchaCode, request);
    }

    /*
     * 登录
     * 需要提交数据
     *   1、用户账号，可以昵称，可以邮箱，唯一处理
     *   2、密码
     *   3、图灵验证码
     *   4、图灵验证码key
     * */
    @PostMapping("/login/{captcha}")
    public ResponseResult login(@PathVariable("captcha") String captcha
            , @RequestBody TbUser user
            ) {

        return userService.doLogin(captcha, user);
    }

    /*
     * 获取图灵验证码
     * */
    @GetMapping("/captcha")
    public void getCaptcha(HttpServletResponse response,HttpServletRequest request) throws Exception {

        userService.createCaptcha(response,request);

    }

    /*
    * 发送邮件
    * 注册（register）：已被注册给出提示
    找回密码（forget）：没有注册提示暂无注册
    修改邮箱(update)：新的邮箱已被注册提示
    * */
    @GetMapping("/verify_code")
    public ResponseResult sendVerifyCode(@RequestParam("type") String type
            , @RequestParam("email") String emailAddress
            , HttpServletRequest request
            , @RequestParam("captchaCode") String captchaCode) {
        log.info("============" + emailAddress);
        return userService.sendEmail(type, request, emailAddress ,captchaCode);
    }

    /*
    * 修改密码：通过旧密码对比更新
    * 找回密码：发送验证码到邮箱或手机，判断验证码是否属于你
    * 1、用户填写邮箱
    * 2、用户获取验证码
    * 3、填写验证码
    * 4、填写新密码
    * 5、提交数据
    *
    * type=forget
    * 注册（register）：已被注册给出提示
    找回密码（forget）：没有注册提示暂无注册
    修改邮箱(update)：新的邮箱已被注册提示
    * 参数：
    *   邮箱和新密码
    *   验证码
    * */
    @PutMapping("/password/{verifyCode}")
    public ResponseResult updatePassword(@PathVariable("verifyCode") String verifyCode, @RequestBody TbUser user) {


        return userService.updateUserPassword(verifyCode,user);
    }

    /*
     * 获取作者信息
     * */
    @GetMapping("/user_info/{userId}")
    public ResponseResult getUserInfo(@PathVariable("userId") String userId) {
        return userService.getUserInfo(userId);
    }

    /*
     * 修改用户信息
     * 可修改信息：：
     *   1、头像
     *   2、密码（单独接口修改）
     *   3、签名
     *   4、email（单独接口修改）
     *
     * */
    @PutMapping("/user_info/{userId}")
    public ResponseResult updateUserInfo(HttpServletResponse response
            , HttpServletRequest request
            , @PathVariable("userId") String userId
            , @RequestBody TbUser user) {
        return userService.updateUserInfo(request, response, userId, user);
    }

    /*
     * 查询用户列表
     * 需要管理员权限
     * */

    @GetMapping("/list")
    @PreAuthorize("@permission.admin()")//判断权限
    public ResponseResult listUsers(
            @RequestParam("page") int page
            , @RequestParam("size") int size
            ,@RequestParam(value = "userName",required = false) String userName,
            @RequestParam(value = "email",required = false) String email,
            HttpServletRequest request,
            HttpServletResponse response) {

        return userService.getListUser(page, size,userName,email,request, response);
    }

    /*
     * 删除用户（逻辑删除）
     * */
//    @PreAuthorize("@permission.admin()")//判断权限
    @DeleteMapping("/{userId}")
    public ResponseResult delUser(HttpServletRequest request, HttpServletResponse response, @PathVariable("userId") String userId) {

        return userService.deleteUserById(userId, request, response);
    }

    /*
     * 检查邮箱是否重复
     * success----已注册
     * faile ----未注册
     * */

    @GetMapping("/email")
    public ResponseResult checkEmail(@RequestParam("email") String email) {
        return userService.checkEmail(email);
    }

    /*
     * 检查用户名是否重复
     * success----已注册
     * faile ----未注册
     * */
    @GetMapping("/user_name")
    public ResponseResult checkUsername(@RequestParam("userName") String userName) {
        return userService.checkUsername(userName);
    }

    /*
    * 邮箱更新
    * 类型：update
    *
    * 用户步骤：
    * 1已经登录
    * 2、输入新的邮箱
    * 3、获取验证码
    * 4、输入验证码
    * 5、提交数据
    *
    * 1、必须登录
    * 2、新的邮箱没有注册过
    * //新邮箱地址，验证码
    * */
    @PutMapping("/email")
    public ResponseResult updateEmail( @RequestParam("email") String email
            ,@RequestParam("verifyCode")String verifyCode,
                                       HttpServletRequest request,HttpServletResponse response){

        return userService.updateEmail(email,verifyCode,request,response);
    }

    /*
    * 退出登录
    *
    * 拿到tokenKey
    * 删除redis里的token
    * 删除mysql里面对应的refresh
    * 删除cookie里的tokenKey
    *
    * */
    @GetMapping("/logout")
    public ResponseResult logout(){

        return userService.doLogout();
    }

    @GetMapping("/check-token")
    public ResponseResult parseToken(HttpServletRequest request,HttpServletResponse response){
        return userService.parseToken(request,response);
    }

    //管理员修改任意用户的密码
    @PreAuthorize("@permission.admin()")//判断权限
    @PutMapping("/reset-password/{userId}")
    public ResponseResult resetPassword(@PathVariable("userId") String userId,@RequestParam("password") String password){
        return userService.resetPassword(userId,password);
    }


}

