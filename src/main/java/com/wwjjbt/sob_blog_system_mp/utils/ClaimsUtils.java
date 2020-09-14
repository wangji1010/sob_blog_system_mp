package com.wwjjbt.sob_blog_system_mp.utils;

import com.wwjjbt.sob_blog_system_mp.pojo.TbUser;
import io.jsonwebtoken.Claims;

import java.util.HashMap;
import java.util.Map;

public class ClaimsUtils {
    /*
    * 获取
    * */
   public static TbUser tbUserToClaims(TbUser user){
       user.getId();
       user.getUserName();
       user.getRoles();
       user.getEmail();
       user.getSign();
       user.getAvatar();
       return user;
   }
   /*
   * 读
   * */
   public static TbUser claimsToUser(Claims claims){
       TbUser user = new TbUser();
       String id = claims.getId();


//       user.setId();
       return user;
   }
}
