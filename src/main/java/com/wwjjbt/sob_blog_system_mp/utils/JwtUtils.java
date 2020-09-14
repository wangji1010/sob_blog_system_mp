package com.wwjjbt.sob_blog_system_mp.utils;

import com.wwjjbt.sob_blog_system_mp.pojo.TbUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

/**
 * jwt工具类
 */
public class JwtUtils {


    public static final String SUBJECT = "onehee";

    public static final long EXPIRE = 60*60*24*60;  //过期时间，毫秒，
    public static final long REFRESH_TOKEN = 60*60*24*30*30;  //过期时间，毫秒，

    //秘钥
    public static final  String APPSECRET = "c60b98a542615d7d26e3724f26356a47";



    /*
    * 生成refreshToken
    * */
    public static String createRefreshToken(String userId){

        if( userId==null){
            return null;
        }
        String token = Jwts.builder().setSubject(SUBJECT)
                .claim("id",userId)

                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+REFRESH_TOKEN))
                .signWith(SignatureAlgorithm.HS256,APPSECRET).compact();

        return token;
    }

    /**
     * 生成jwt
     * @param user
     * @return
     */
    public static String createJsonWebToken(TbUser user){

        if(user == null || user.getId() == null || user.getUserName() == null
                || user.getAvatar()==null||user.getRoles()==null||user.getEmail()==null||user.getSign()==null){
            return null;
        }
        String token = Jwts.builder().setSubject(SUBJECT)
                .claim("id",user.getId())
                .claim("name",user.getUserName())
                .claim("avatar",user.getAvatar())
                .claim("role",user.getRoles())
                .claim("email",user.getEmail())
                .claim("sign",user.getSign())


                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+EXPIRE))
                .signWith(SignatureAlgorithm.HS256,APPSECRET).compact();

        return token;
    }




    /**
     * 校验token
     * @param token
     * @return
     */
    public static Claims checkJWT(String token ){

        try{
            final Claims claims =  Jwts.parser().setSigningKey(APPSECRET).
                    parseClaimsJws(token).getBody();
            return  claims;

        }catch (Exception e){ }
        return null;

    }



}