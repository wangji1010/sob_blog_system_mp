package com.wwjjbt.sob_blog_system_mp.mapper;

import com.wwjjbt.sob_blog_system_mp.pojo.TbCategories;
import com.wwjjbt.sob_blog_system_mp.pojo.TbLooper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
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
public interface TbLooperMapper extends BaseMapper<TbLooper> {

    @Select("select * from tb_looper")
    ArrayList<TbLooper> selectAll();
}
