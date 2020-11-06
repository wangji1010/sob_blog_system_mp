package com.wwjjbt.sob_blog_system_mp.service;

import com.wwjjbt.sob_blog_system_mp.pojo.TbLostPost;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wwjjbt.sob_blog_system_mp.response.ResponseResult;
import org.springframework.boot.configurationprocessor.json.JSONException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Key;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wangji
 * @since 2020-10-16
 */
public interface TbLostPostService extends IService<TbLostPost> {

    ResponseResult insertPost(TbLostPost lostPost ,String key, HttpServletRequest request, HttpServletResponse response) throws JSONException;

    ResponseResult deletePostById(String id);

    ResponseResult updatePostState(String id);

    ResponseResult selectPostByPage(int page, int size);

    ResponseResult updatePost(String id, TbLostPost lostPost);

    ResponseResult selectOneById(String id);



    ResponseResult updateTopById(String id, HttpServletRequest request, HttpServletResponse response);

    ResponseResult selectTop();
}
