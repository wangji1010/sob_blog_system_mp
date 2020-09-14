package com.wwjjbt.sob_blog_system_mp.utils;

import com.wwjjbt.sob_blog_system_mp.pojo.TbUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtTokenUtils {

    //用于签名的私钥
    private static final String PRIVATE_KEY = "17CS3Flight";
    //签发者
    private static final String ISS = "wangji";

    //过期时间1小时
    private static final long EXPIRATION_ONE_HOUR = 3600L;
    //过期时间1天
    private static final long EXPIRATION_ONE_DAY = 604800L;

    public static final String ID="id";
    public static final String USER_NAME="userName";
    public static final String ROLES="roles";
    public static final String AVATAR="avatar";
    public static final String EMAIL="email";
    public static final String SIGN="sign";

    /**
     * 生成Token
     * @param user
     * @return
     */
    public static String createToken(TbUser user, int expireTimeType){
        //过期时间
        long expireTime = 0;
        if (expireTimeType == 0){
            expireTime = EXPIRATION_ONE_HOUR;
        }else {
            expireTime = EXPIRATION_ONE_DAY;
        }

        //Jwt头
        Map<String,Object> header = new HashMap<>();
        header.put("typ","JWT");
        header.put("alg","HS256");
        Map<String,Object> claims = new HashMap<>();
        //自定义有效载荷部分
        claims.put(ID,user.getId());
        claims.put(USER_NAME,user.getUserName());
        claims.put(ROLES,user.getRoles());
        claims.put(AVATAR,user.getAvatar());
        claims.put(EMAIL,user.getEmail());
        claims.put(SIGN,user.getSign());

        return Jwts.builder()
                //发证人
                .setIssuer(ISS)
                //Jwt头
                .setHeader(header)
                //有效载荷
                .setClaims(claims)
                //设定签发时间
                .setIssuedAt(new Date())
                //设定过期时间
                .setExpiration(new Date(System.currentTimeMillis() + expireTime * 1000))
                //使用HS256算法签名，PRIVATE_KEY为签名密钥
                .signWith(SignatureAlgorithm.HS256,PRIVATE_KEY)
                .compact();
    }

    /**
     * 验证Token，返回数据只有id和account和type的User对象
     * @param token
     * @return
     */
    public static TbUser checkToken(String token){
        //解析token后，从有效载荷取出值
        String id = (String) getClaimsFromToken(token).get(ID);
        String username = (String) getClaimsFromToken(token).get(USER_NAME);
        String avatar = (String) getClaimsFromToken(token).get(AVATAR);
        String roles = (String) getClaimsFromToken(token).get(ROLES);
        String email = (String) getClaimsFromToken(token).get(EMAIL);
        String sign = (String) getClaimsFromToken(token).get(SIGN);
        //封装为User对象
        TbUser user = new TbUser();
        user.setId(id);
        user.setUserName(username);
        user.setAvatar(avatar);
        user.setRoles(roles);
        user.setEmail(email);
        user.setSign(sign);

        return user;
    }

    /**
     * 获取有效载荷
     * @param token
     * @return
     */
    public static Claims getClaimsFromToken(String token){
        Claims claims = null;
        try {
            claims = Jwts.parser()
                    //设定解密私钥
                    .setSigningKey(PRIVATE_KEY)
                    //传入Token
                    .parseClaimsJws(token)
                    //获取载荷类
                    .getBody();
        }catch (Exception e){
            return null;
        }
        return claims;
    }

}
