package com.wwjjbt.sob_blog_system_mp.mapper;

import com.wwjjbt.sob_blog_system_mp.pojo.TbLostComment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wwjjbt.sob_blog_system_mp.response.ResponseResult;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wangji
 * @since 2020-10-26
 */
@Repository
public interface TbLostCommentMapper extends BaseMapper<TbLostComment> {

    int selectCommentCount(String id);

    int selectReplyCount(String parentId);

    ArrayList<TbLostComment> getReplyList(String parentId);
}
