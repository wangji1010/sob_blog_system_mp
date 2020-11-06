package com.wwjjbt.sob_blog_system_mp.mapper;

import com.wwjjbt.sob_blog_system_mp.pojo.TbLostGreat;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wangji
 * @since 2020-10-18
 */
@Repository
public interface TbLostGreatMapper extends BaseMapper<TbLostGreat> {

    TbLostGreat selectGreatBya_u(String a_id, String u_id);

    void deleteGreatPoint(String id);
}
