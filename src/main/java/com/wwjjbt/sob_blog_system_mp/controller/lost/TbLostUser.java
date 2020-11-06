package com.wwjjbt.sob_blog_system_mp.controller.lost;

import com.wwjjbt.sob_blog_system_mp.pojo.TbLostWxuser;
import com.wwjjbt.sob_blog_system_mp.pojo.TbUser;
import com.wwjjbt.sob_blog_system_mp.response.ResponseResult;
import com.wwjjbt.sob_blog_system_mp.service.TbLostUserService;
import com.wwjjbt.sob_blog_system_mp.service.TbLostWxuserService;
import com.wwjjbt.sob_blog_system_mp.service.TbUserService;
import com.wwjjbt.sob_blog_system_mp.utils.RedisUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.annotation.RequestScope;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/lost/user")
public class TbLostUser {

    @Autowired
    private TbLostUserService lostUserService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
     private TbLostWxuserService lostWxuserService;

    //时间
    int time7 = 60*60*24*7;

    /*
    * 获取当前自己的帖子
    * */
    @GetMapping("/getMinePost/{key}")
    public ResponseResult getMinePost(@PathVariable String key) throws JSONException {
        return lostWxuserService.getMinePost( key);
    }
    /*
    * 获取当前自己的失物信息
    * */
    @GetMapping("/getMineLost/{key}")
    public ResponseResult getMineLost(@PathVariable String key) throws JSONException {
        return lostWxuserService.getMineLost( key);
    }

    /*
    发送图灵验证码
    * */
    @GetMapping("/captcha")
    public void getCaptcha(HttpServletResponse response, HttpServletRequest request) throws Exception {

        lostUserService.createCaptcha(response,request);

    }

    /*
    * wx 接口
    * */
    @GetMapping("/login/{code}")
    public Map<String,Object> wxLogin(@PathVariable("code") String code){

        Map<String,Object> res = new HashMap<>();
        res.put("status",200);

        String appId = "wxd498946c32263fb2";
        String secret = "1c1f39a03cfff458838a870711f8c43f";
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid="
                +appId+"&secret="+secret+"&js_code="
                +code+"&grant_type=authorization_code";
        String jsonData = restTemplate.getForObject(url, String.class);
        if (StringUtils.contains(jsonData,"errcode")){
            res.put("status",500);
            res.put("message","校验失败");
            return res;
        }
        String md5Key = "LOGIN"+ DigestUtils.md5Hex(jsonData)+"WX";
        //保存到redis WX_LOGIN_(MD5值)
        String redisKey = "WX_LOGIN"+md5Key;
        redisUtil.set(redisKey,jsonData,time7);
        res.put("ticket",redisKey);
        return res;
    }

    @PostMapping("/register")
    public ResponseResult registerWX(@RequestParam("key") String key
                , @RequestParam("nickName") String nickName
                , @RequestParam("gender") Integer gender
                , @RequestParam("avatarUrl") String avatarUrl) throws JSONException {

        return lostWxuserService.registerWX(key,nickName,gender,avatarUrl);
    }

    @GetMapping("/userInfo/{key}")
    public ResponseResult getUserInfo(@PathVariable("key") String key) throws JSONException {
        return lostWxuserService.getUserInfo(key);
    }



}
