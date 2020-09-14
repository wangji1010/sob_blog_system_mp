package com.wwjjbt.sob_blog_system_mp.mapper;

import com.wwjjbt.sob_blog_system_mp.pojo.TbArticle;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wwjjbt.sob_blog_system_mp.pojo.TbArticleNoContent;
import com.wwjjbt.sob_blog_system_mp.pojo.TbCategories;
import com.wwjjbt.sob_blog_system_mp.response.ResponseResult;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wangji
 * @since 2020-06-21
 */
@Repository
public interface TbArticleMapper extends BaseMapper<TbArticle> {

    @Update("update `tb_article` set `state` = '0' where `id`=#{articleId}")
    int deleteArticleByState(String articleId);

    @Update("update `tb_article` set `state` = '3' where `id`=#{articleId}")
    int updateTopById(String articleId);

    @Update("update `tb_article` set `state` = '1' where `id`=#{articleId}")
    int updateNotTopById(String articleId);

    @Select("select labels from tb_article where `id`=#{articleId}")
    String listArticlelabelById(String articleId);


    @Select("select * from tb_article where `id`!='#{orginalArticleId}' and state='1' or state='3' order by `create_time` desc limit #{size}")
    List<TbArticle> getListLastArticleBySize(String orginalArticleId,int size);

    List<TbCategories> getlistCategories();


}
