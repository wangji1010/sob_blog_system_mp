package com.wwjjbt.sob_blog_system_mp;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.wwjjbt.sob_blog_system_mp.mapper.TbLostCategoriesMapper;
import com.wwjjbt.sob_blog_system_mp.mapper.TbLostWxuserMapper;
import com.wwjjbt.sob_blog_system_mp.pojo.TbLostCategories;
import com.wwjjbt.sob_blog_system_mp.pojo.TbLostWxuser;
import com.wwjjbt.sob_blog_system_mp.utils.RedisUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LostTest {

    @Autowired
    private TbLostCategoriesMapper lostCategoriesMapper;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private TbLostWxuserMapper lostWxuserMapper;

    @Test
    public void test02(){
        String userId = lostWxuserMapper.getUserId("oVaW-4kGLUc7khL5sinVcYDX1OMs");
        System.out.println(userId);
    }

    @Test
    public void get(){
        TbLostWxuser tbLostWxuser = lostWxuserMapper.selectByOS("oVaW-4kGLUc7khL5sinVcYDX1OMs");
        System.out.println(tbLostWxuser);
    }

    @Test
    public void getkey() throws JSONException {
        Object wx = redisUtil.get("WX_LOGINLOGINa80fbb18a7d7e8984f7e6d19ed107893WX");
        JSONObject jsonObject = new JSONObject((String) wx);
        System.out.println(jsonObject.get("openid"));

        System.out.println(wx);
        System.out.println();
    }

    @Test
    public void test01(){
        TbLostCategories categories = lostCategoriesMapper.selectByName("hud");
        System.out.println(categories);
    }

}
