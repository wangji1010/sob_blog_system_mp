package com.wwjjbt.sob_blog_system_mp.service;

import com.wwjjbt.sob_blog_system_mp.pojo.TbArticle;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wwjjbt.sob_blog_system_mp.response.ResponseResult;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wangji
 * @since 2020-06-21
 */
@Service
public interface TbArticleService  {

    ResponseResult addArticle(HttpServletRequest request, HttpServletResponse response, TbArticle article) throws IOException;

    ResponseResult getListArticle(int page, int size, String keywords, String categoryId,String state);

    ResponseResult getArticleById(String articleId) throws IOException;

    ResponseResult updateArticleById(String articleId, TbArticle article) throws IOException;

    ResponseResult deleteById(String articleId) throws IOException;

    ResponseResult deleteByState(String articleId);

    ResponseResult topArticle(String articleId);

    ResponseResult getTopArticle();

    ResponseResult listRecommendArticleId(String articleId, int zize);

    ResponseResult listArticles(int pages, int size, String label);

    ResponseResult getlistCategories();

    ResponseResult getArticleLabels(int size);


}
