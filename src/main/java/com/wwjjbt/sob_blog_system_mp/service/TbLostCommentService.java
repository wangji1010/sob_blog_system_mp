package com.wwjjbt.sob_blog_system_mp.service;

import com.wwjjbt.sob_blog_system_mp.pojo.TbLostComment;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wwjjbt.sob_blog_system_mp.response.ResponseResult;
import org.springframework.boot.configurationprocessor.json.JSONException;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wangji
 * @since 2020-10-26
 */
public interface TbLostCommentService extends IService<TbLostComment> {

    ResponseResult addComment( String key ,TbLostComment lostComment) throws JSONException;

    ResponseResult getCount(String id);

    ResponseResult getCommentList(String id, int page, int size);

    ResponseResult getReplyCount(String parentId);

    ResponseResult getReplyList(String replyList);
}
