package com.wwjjbt.sob_blog_system_mp.mapper;

import com.wwjjbt.sob_blog_system_mp.pojo.TbCategories;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
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
@Repository
@Mapper
public interface TbCategoriesMapper extends BaseMapper<TbCategories> {

    @Select("SELECT * FROM tb_categories ORDER BY c_order DESC")
    List<TbCategories> selectAll();

    @Select("SELECT * FROM tb_categories WHERE tb_categories.id = #{id}")
    TbCategories selectByupId(String id);

    @Update("UPDATE tb_categories SET c_name=#{cName}, pinyin=#{pinyin}, description=#{description}, c_order=#{cOrder},c_status=#{cStatus},create_time=#{createTime},update_time=#{updateTime} WHERE id=#{id}")
    void updateByupId(TbCategories cate);

    void updateCatById(TbCategories cate);

    List<TbCategories> getCategorysByState();


}
