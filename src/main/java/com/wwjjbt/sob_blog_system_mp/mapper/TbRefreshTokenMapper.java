package com.wwjjbt.sob_blog_system_mp.mapper;

import com.wwjjbt.sob_blog_system_mp.pojo.TbRefreshToken;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wangji
 * @since 2020-06-27
 */
@Repository
public interface TbRefreshTokenMapper extends BaseMapper<TbRefreshToken> {

    @Update("update `tb_refresh_token` set `mobile_token_key` = '' where `mobile_token_key` = #{tokenKey}")
    void deleteMobileTokenKey(String tokenKey);

    @Update("update `tb_refresh_token` set `token_key` = '' where `token_key` = #{tokenKey}")
    void deletePcTokenKey(String tokenKey);
}
