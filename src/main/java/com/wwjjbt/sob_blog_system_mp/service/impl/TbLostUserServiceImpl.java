package com.wwjjbt.sob_blog_system_mp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wf.captcha.SpecCaptcha;
import com.wf.captcha.base.Captcha;
import com.wwjjbt.sob_blog_system_mp.controller.lost.TbLostUser;
import com.wwjjbt.sob_blog_system_mp.pojo.TbUser;
import com.wwjjbt.sob_blog_system_mp.response.ResponseResult;
import com.wwjjbt.sob_blog_system_mp.service.TbLostUserService;
import com.wwjjbt.sob_blog_system_mp.utils.Constrants;
import com.wwjjbt.sob_blog_system_mp.utils.CookieUtils;
import com.wwjjbt.sob_blog_system_mp.utils.RedisUtil;
import com.wwjjbt.sob_blog_system_mp.utils.TextUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Random;

@Service
@Slf4j
public class TbLostUserServiceImpl implements TbLostUserService {


    //图灵验证码，字体
    public static final int[] CAPTCHA_FONT_TYPES = {Captcha.FONT_1
            , Captcha.FONT_2
            , Captcha.FONT_3
            , Captcha.FONT_4
            , Captcha.FONT_5
            , Captcha.FONT_6
            , Captcha.FONT_7
            , Captcha.FONT_8
            , Captcha.FONT_9
            , Captcha.FONT_10
    };
    public static final int[] CAPTCHA_TYPE = {Captcha.TYPE_DEFAULT
            , Captcha.TYPE_ONLY_NUMBER
            , Captcha.TYPE_ONLY_CHAR
            , Captcha.TYPE_ONLY_UPPER
            , Captcha.TYPE_ONLY_LOWER
            , Captcha.TYPE_NUM_AND_UPPER
    };

    @Autowired
    private Random random;

    @Autowired
    private RedisUtil redisUtil;

    /*
    * 发送图灵验证码
    *
    * 小程序专有接口不包含cookie
    *
    * */
    @Override
    public void createCaptcha(HttpServletResponse response, HttpServletRequest request) {


        try {
//            key = Long.parseLong(captchaKey);
            //处理
            // 设置请求头为输出图片类型
            response.setContentType("image/gif");
            response.setHeader("Pragma", "No-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0);

            // 三个参数分别为宽、高、位数
            SpecCaptcha specCaptcha = new SpecCaptcha(200, 50, 5);
            //随机字体
            int fontIndex = random.nextInt(CAPTCHA_FONT_TYPES.length);
            log.info("================index===>>>" + fontIndex);
            // 设置字体
            specCaptcha.setFont(fontIndex/*new Font("Captcha.FONT_1", Font.PLAIN, 32)*/);  // 有默认字体，可以不用设置

            int typeIndex = random.nextInt(CAPTCHA_TYPE.length);
            // 设置类型，纯数字、纯字母、字母数字混合
            specCaptcha.setCharType(typeIndex);
            String content = specCaptcha.text().toLowerCase();
            log.info("=============================>>>>>>" + content);
            redisUtil.set(Constrants.User.KEY_CAPTCHA_CONTENT, content, 60 * 10);
            // 输出图片流
            specCaptcha.out(response.getOutputStream());

        } catch (Exception e) {
            return;
        }
    }

    /*
    * 小程序端专用 登录接口
    * */
    @Override
    public ResponseResult doLogin(String captcha, TbUser user) {

        return null;
    }
}
