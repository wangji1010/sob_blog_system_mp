package com.wwjjbt.sob_blog_system_mp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wwjjbt.sob_blog_system_mp.mapper.TbLostArticleMapper;
import com.wwjjbt.sob_blog_system_mp.mapper.TbLostPostMapper;
import com.wwjjbt.sob_blog_system_mp.pojo.TbLostArticle;
import com.wwjjbt.sob_blog_system_mp.pojo.TbLostPost;
import com.wwjjbt.sob_blog_system_mp.pojo.TbLostWxuser;
import com.wwjjbt.sob_blog_system_mp.mapper.TbLostWxuserMapper;
import com.wwjjbt.sob_blog_system_mp.response.ResponseResult;
import com.wwjjbt.sob_blog_system_mp.service.TbLostWxuserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wwjjbt.sob_blog_system_mp.utils.IdWorker;
import com.wwjjbt.sob_blog_system_mp.utils.RedisUtil;
import com.wwjjbt.sob_blog_system_mp.utils.TextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wangji
 * @since 2020-10-24
 */
@Service
public class TbLostWxuserServiceImpl extends ServiceImpl<TbLostWxuserMapper, TbLostWxuser> implements TbLostWxuserService {

    @Autowired
    private TbLostWxuserMapper lostWxuserMapper;

    @Autowired
    private TbLostPostMapper lostPostMapper;

    @Autowired
    private TbLostArticleMapper lostArticleMapper;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private IdWorker idWorker;


    /*
    * 当数据提交过来应该先检查 数据库当中是否有该用户的信息，
    * 如果存在该用户的信息，就无需注册进表，只需要将用户的个人信息更新到数据当中即可
    * */
    @Override
    public ResponseResult registerWX(String key,String nickName,Integer gender,String avatarUrl) throws JSONException {

        System.out.println(key);
        TbLostWxuser lostWxuser = new TbLostWxuser();
        String session_key;
        String openId;
        //从redis中获取 到openId 和 session_key
        Object wx = redisUtil.get(key);

            if (wx==null){
                return ResponseResult.failed("用户未授权");
            }
            if (TextUtils.isEmpty(nickName)){
                return ResponseResult.failed("用户名不能为空");
            }
            if (TextUtils.isEmpty(avatarUrl)){
                return ResponseResult.failed("头像不能为空");
            }
            if (gender==null){
                return ResponseResult.failed("性别不能为空");
            }
            JSONObject jsonObject = new JSONObject((String) wx);
            session_key = (String) jsonObject.get("session_key");
            openId = (String) jsonObject.get("openid");

            //查询用户的信息
            TbLostWxuser tbLostWxuser = lostWxuserMapper.selectByOS(openId);
            if (tbLostWxuser!=null){
                //已经注册过更新数据即可
                tbLostWxuser.setGender(gender);
                tbLostWxuser.setUserAvatar(avatarUrl);
                tbLostWxuser.setNickName(nickName);
                tbLostWxuser.setSessionKey(session_key);
                //更新
                lostWxuserMapper.updateById(tbLostWxuser);
                return ResponseResult.success("用户数据已经更新");
            }

            lostWxuser.setId(idWorker.nextId()+"");
            lostWxuser.setSessionKey(session_key);
            lostWxuser.setOppenId(openId);
            lostWxuser.setCreateTime(new Date());
            lostWxuser.setNickName(nickName);
            lostWxuser.setGender(gender);
            lostWxuser.setUserAvatar(avatarUrl);
            //入库
            lostWxuserMapper.insert(lostWxuser);
        return ResponseResult.success("注册成功");
    }

    /*
    * 获取用户信息 根据key 可以获得 session  和 openid
    * */
    @Override
    public ResponseResult getUserInfo(String key) throws JSONException {
        //从redis中获取 到openId 和 session_key
        Object wx = redisUtil.get(key);
        if (wx==null){
            return ResponseResult.failed("用户未授权");
        }
            JSONObject jsonObject = new JSONObject((String) wx);
            String  session_key = (String) jsonObject.get("session_key");
            String openId = (String) jsonObject.get("openid");

         TbLostWxuser tbLostWxuser = lostWxuserMapper.selectByOS( openId);
//        QueryWrapper<TbLostWxuser> wrapper = new QueryWrapper<>();
//        wrapper.eq("oppen_id",openId);
//        wrapper.eq("session_key",session_key);
//        TbLostWxuser tbLostWxuser = lostWxuserMapper.selectOne(wrapper);

        System.out.println(tbLostWxuser);

        return ResponseResult.success("获取用户信息成功").setData(tbLostWxuser);
    }

    /*
    * 获取自己发布的帖子
    * */
    @Override
    public ResponseResult getMinePost(String key) throws JSONException {
        if (!TextUtils.isEmpty(key)){
            //解析key
            //从redis中获取 到openId 和 session_key
            Object wx = redisUtil.get(key);
            if (wx==null){
                return ResponseResult.failed("用户未授权");
            }
            JSONObject jsonObject = new JSONObject((String) wx);
            String openId = (String) jsonObject.get("openid");
            //通过openId获取到用户的id
             String userId = lostWxuserMapper.getUserId(openId);
             //通过userId查询帖子
             List<TbLostPost> list  = lostPostMapper.selectListAll(userId);
             if (list==null){
                 return ResponseResult.failed("你还没有发布哦");
             }
             return ResponseResult.success("获取发布成功").setData(list);
        }

        return ResponseResult.failed("key不能为空");
    }

    /*
    * 获取自己的失物信息
    * */
    @Override
    public ResponseResult getMineLost(String key) {
        //解析key
        //从redis中获取 到openId 和 session_key
        Object wx = redisUtil.get(key);
        if (wx==null){
            return ResponseResult.failed("用户未授权");
        }
        JSONObject jsonObject = null;

        try {
            jsonObject = new JSONObject((String) wx);
            String openId = (String) jsonObject.get("openid");
            //获取用户id
            String userId = lostWxuserMapper.getUserId(openId);
            //通过用户id获取失物信息
             List<TbLostArticle> list = lostArticleMapper.getMineLost(userId);
             if (list==null){
                 return ResponseResult.failed("你还没有发布哦");
             }
            return ResponseResult.success("获取失物成功").setData(list);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }


}
