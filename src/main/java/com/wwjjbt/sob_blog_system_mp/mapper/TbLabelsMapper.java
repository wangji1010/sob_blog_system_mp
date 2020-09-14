package com.wwjjbt.sob_blog_system_mp.mapper;

import com.wwjjbt.sob_blog_system_mp.pojo.TbLabels;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
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
@Mapper
@Repository
public interface TbLabelsMapper extends BaseMapper<TbLabels> {

    @Select("select * from tb_labels where `name`=#{label}")
    TbLabels getLabelByName(String label);

    @Update("update tb_labels set `count` = `count`+1 where `name` = #{labelName}")
    int updateCountByName(String labelName);

    List<TbLabels> getLabelsAll(int size);
}
