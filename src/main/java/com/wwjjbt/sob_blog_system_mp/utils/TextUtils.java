package com.wwjjbt.sob_blog_system_mp.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextUtils {
    public static boolean isEmpty(String text){

        return text==null||text.length()==0;
    }
    public static boolean isEmailAddress(String emailAddress){
        String regEx1 = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        Pattern p = Pattern.compile(regEx1);
        Matcher m = p.matcher(emailAddress);

        return m.matches();
       /* if(m.matches()){
            return true;
        }else{
            return false;
        }*/

    }
}
