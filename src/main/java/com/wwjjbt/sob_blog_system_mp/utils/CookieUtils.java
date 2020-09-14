package com.wwjjbt.sob_blog_system_mp.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieUtils {

    public static final int default_age = 60*60*24*365;

    static final String domain = "localhost";

    /*
    * 设置cookie值
    * */
    public static void setUpCookie(HttpServletResponse response,String key,String value){
       setUpCookie(response,key,value,default_age);//-1一直保存
    }
    public static void setUpCookie(HttpServletResponse response,String key,String value,int age){
        Cookie cookie = new Cookie(key,value);
        cookie.setPath("/");
        //域名根据当前的自己的域名设置
//        cookie.setDomain(domain);
        cookie.setMaxAge(age);
        response.addCookie(cookie);
    }

    /*
    * 删除cookie
    * */
    public static void deleteCookie(  HttpServletResponse response, String key){
       setUpCookie(response,key,null,0);
    }

    /*
    * 获取Cookie
    * */
    public static String getCookie(HttpServletRequest request,String key){
        Cookie[] cookies = request.getCookies();
        if (cookies!=null){
            for (Cookie cookie : cookies) {
                if (key.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }

        return null;
    }
}
