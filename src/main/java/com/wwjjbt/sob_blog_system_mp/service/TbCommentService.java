package com.wwjjbt.sob_blog_system_mp.service;

import com.wwjjbt.sob_blog_system_mp.pojo.TbComment;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wwjjbt.sob_blog_system_mp.response.ResponseResult;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wangji
 * @since 2020-06-21
 */
public interface TbCommentService {


    ResponseResult addComment(TbComment comment);

    ResponseResult listCommend(String articleId, int page, int size);

    ResponseResult deleteCommendById(String commentId);

    ResponseResult listCommends(String article_id,int page, int size);

    ResponseResult topComment(String commentId);
}
