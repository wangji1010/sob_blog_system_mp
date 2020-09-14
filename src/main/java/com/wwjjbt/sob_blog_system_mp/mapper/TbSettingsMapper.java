package com.wwjjbt.sob_blog_system_mp.mapper;

import com.wwjjbt.sob_blog_system_mp.pojo.TbSettings;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wangji
 * @since 2020-06-21
 */
@Repository
public interface TbSettingsMapper extends BaseMapper<TbSettings> {


    @Select("select * from tb_settings where `key`=#{key}")
    public TbSettings hasKey(String key);

    @Insert("insert  into tb_settings (id,`key`,`value`,create_time,update_time) values (#{id},#{key},#{value},#{createTime},#{updateTime})")
    public void insertinto(TbSettings settings);

    @Select("select * from tb_settings where `key` = #{title}")
    public TbSettings getWebTitle(String title);

    @Update("update tb_settings set `value`=#{value} where `key`=#{key}")
    void updateSettingsById(String value,String key);

    @Update("update tb_settings set `value`=#{webTitle.value} where `key`=#{webTitle.key}")
    void updateSettings(TbSettings webTitle);

    void updateSettingsMapper(Map<String,Object> map);

    void updateKeyWords(Map<String,Object> map);
    void updateDesc(Map<String,Object> map);
}
