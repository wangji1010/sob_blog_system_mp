package com.wwjjbt.sob_blog_system_mp.service.impl;

import com.wwjjbt.sob_blog_system_mp.pojo.TbUser;
import com.wwjjbt.sob_blog_system_mp.service.TbUserService;
import com.wwjjbt.sob_blog_system_mp.utils.Constrants;
import com.wwjjbt.sob_blog_system_mp.utils.CookieUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service("permission")
public class PermissionService {

    @Autowired
    private TbUserService userService;

    /*
    * 判断是否是管理员
    * */
    public boolean admin(){
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        HttpServletResponse response = requestAttributes.getResponse();
        String tokenKey = CookieUtils.getCookie(request, Constrants.User.COOKIE_TOKE_KEY);
        if (tokenKey==null){
            //未登录
            return false;
        }

        TbUser currentUser =userService.checkUser(request, response);
        if (currentUser==null) {
            return false;
        }

        if (currentUser.getRoles().equals(Constrants.User.ROLE_ADMIN)) {
            //管理员
            return true;
        }
        return false;

    }




}
