package com.wwjjbt.sob_blog_system_mp.service;

import com.wwjjbt.sob_blog_system_mp.pojo.TbLostArticle;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wwjjbt.sob_blog_system_mp.response.ResponseResult;
import org.springframework.boot.configurationprocessor.json.JSONException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wangji
 * @since 2020-10-16
 */
public interface TbLostArticleService extends IService<TbLostArticle> {

    ResponseResult insertArticle(TbLostArticle lostArticle,String key) throws JSONException;

    ResponseResult updateByState(String id);

    ResponseResult deleteByID(String id);

    ResponseResult selectArticleById(String id);


    ResponseResult selectByPage(int page, int size);

    ResponseResult selectByUser();

    ResponseResult selectByLF(int page, int size, int type);
}
