package com.wwjjbt.sob_blog_system_mp.mapper;

import com.wwjjbt.sob_blog_system_mp.pojo.TbLostArticle;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wangji
 * @since 2020-10-16
 */
@Repository
public interface TbLostArticleMapper extends BaseMapper<TbLostArticle> {

    void updateByState(String id);

    void updateRollback(String id);

    ArrayList<TbLostArticle> getMineLost(String userId);
}
