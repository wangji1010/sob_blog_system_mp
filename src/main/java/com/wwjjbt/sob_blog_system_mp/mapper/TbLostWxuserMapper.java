package com.wwjjbt.sob_blog_system_mp.mapper;

import com.wwjjbt.sob_blog_system_mp.pojo.TbLostWxuser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wangji
 * @since 2020-10-24
 */
@Repository
public interface TbLostWxuserMapper extends BaseMapper<TbLostWxuser> {

    TbLostWxuser selectByOS(String oppen_id);


    @Select("select id from tb_lost_wxuser where oppen_id=#{openId}")
    String getUserId(String openId);
}
