package com.wwjjbt.sob_blog_system_mp.mapper;

import com.wwjjbt.sob_blog_system_mp.pojo.TbLostPost;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wangji
 * @since 2020-10-16
 */
@Repository
public interface TbLostPostMapper extends BaseMapper<TbLostPost> {

    void updatePostState(String id);

    void updateTopById(String id);

    void updateLowById(String id);

    List<TbLostPost> selectTop();

    void updateViewCount(String id, Integer count);

    ArrayList<TbLostPost> selectListAll(String userId);
}
