package com.wwjjbt.sob_blog_system_mp.service;

import com.wwjjbt.sob_blog_system_mp.pojo.TbLostWxuser;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wwjjbt.sob_blog_system_mp.response.ResponseResult;
import org.springframework.boot.configurationprocessor.json.JSONException;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wangji
 * @since 2020-10-24
 */
public interface TbLostWxuserService extends IService<TbLostWxuser> {

    ResponseResult registerWX(String key,String nickName,Integer gender,String avatarUrl) throws JSONException;

    ResponseResult getUserInfo(String key) throws JSONException;

    ResponseResult getMinePost(String key) throws JSONException;

    ResponseResult getMineLost(String key);
}
