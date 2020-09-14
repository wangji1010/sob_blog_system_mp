package com.wwjjbt.sob_blog_system_mp.controller;

import com.wf.captcha.SpecCaptcha;
import com.wf.captcha.base.Captcha;
import com.wwjjbt.sob_blog_system_mp.pojo.TestUser;
import com.wwjjbt.sob_blog_system_mp.response.ResponseResult;
import com.wwjjbt.sob_blog_system_mp.response.ResponseState;

import com.wwjjbt.sob_blog_system_mp.service.SearchService;
import com.wwjjbt.sob_blog_system_mp.service.impl.TestEsServiceImpl;
import com.wwjjbt.sob_blog_system_mp.utils.Constrants;
import com.wwjjbt.sob_blog_system_mp.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.util.logging.Logger;

@Slf4j
@RestController
public class TestController {

//    public static final Logger log= LoggerFactory.getLogger(TestController.class);

    @GetMapping("/hello")
    public ResponseResult getHello() {
        String value = (String) redisUtil.get("key_captcha_content_123456");
        log.info("===============>>>>>>" + value);
        return ResponseResult.success().setData(value);
    }

    @GetMapping("/test_login")
    public ResponseResult login() {
        TestUser user = new TestUser("admin", 18, "湖南");
        return ResponseResult.success("登录成功").setData(user);
    }

    /*
     *
     * */

    @Autowired
    private RedisUtil redisUtil;

    @RequestMapping("/captcha")
    public void captcha(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 设置请求头为输出图片类型
        response.setContentType("image/gif");
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);

        // 三个参数分别为宽、高、位数
        SpecCaptcha specCaptcha = new SpecCaptcha(130, 48, 5);
        // 设置字体
        specCaptcha.setFont(new Font("Captcha.FONT_1", Font.PLAIN, 32));  // 有默认字体，可以不用设置
        // 设置类型，纯数字、纯字母、字母数字混合
        specCaptcha.setCharType(Captcha.TYPE_ONLY_NUMBER);
        String content = specCaptcha.text().toLowerCase();
        log.info("=============================>>>>>>" + content);
        // 验证码存入session
//        request.getSession().setAttribute("captcha", specCaptcha.text().toLowerCase());
//          保存到redis
        redisUtil.set(Constrants.User.KEY_CAPTCHA_CONTENT + "123456", content, 60 * 5);
        // 输出图片流
        specCaptcha.out(response.getOutputStream());
    }

    /*
     * 测试es
     * */
    @Resource
    private SearchService searchService;

    @GetMapping("/search/{keywords}/{pages}/{size}/{cateforyId}/{sort}")
    public ResponseResult search(@PathVariable("keywords") String keywords
            , @PathVariable("pages") int pages
            , @PathVariable("size") int size
            , @PathVariable(required = false) String categoryId
            , @PathVariable(required = false) String sort) throws IOException {
        return searchService.search(keywords,pages,size,categoryId,sort);
    }
}
